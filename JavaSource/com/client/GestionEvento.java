package com.client;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dao.Semestre_EventoDAO;
import com.dao.Tutor_EventoDAO;
import com.dto.ConvocatoriaDTO;
import com.dto.EventoDTO;
import com.entities.Evento;
import com.entities.Itr;
import com.entities.Semestre;
import com.entities.Tutor;
import com.entities.Tutor_Evento;
import com.enums.EstadoEvento;
import com.enums.EstadoItr;
import com.enums.Modalidad;
import com.enums.RolTutor;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.DepartamentoService;
import com.services.EventoService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.services.SemestreService;
import com.services.TutorService;
import com.services.Tutor_EventoService;
import com.services.UsuarioService;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionEvento")
public class GestionEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	private static final Logger logger = LoggerFactory.getLogger(GestionEvento.class);

	private com.utils.ValidationError validationError = new com.utils.ValidationError();

	@Inject
	private EventoService eventoService;
	@Inject
	private DepartamentoService departamentoService;
	@Inject
	private LocalidadService localidadService;
	@Inject
	private ItrService itrService;
	@Inject
	private SemestreService semestreService;
	@Inject
	private TutorService tutorService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private GestionAutenticacion autenticacion;
    @Inject
    private Tutor_EventoService teService;

	@EJB
	Semestre_EventoDAO semEvDAO;

	

	private EventoDTO eventoSeleccionado = new EventoDTO();
	
	private EventoDTO eventoNuevo = new EventoDTO();
	
	private long idUsuario;
	private TipoUsuario tipoUsuario;
	

	private long idItrSeleccionado;

	private List<Evento> listaEventos;
	private List<Semestre> listaSemestres;
	private List<Tutor> listaTutores;
	private List<Long> listaIdTutores;
	private List<Long> listaIdTutores2;

	private EstadoEvento estadoFilter;
	private Modalidad modalidadFilter;

	private String prueba;

	@PostConstruct
	private void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		
		listaEventos = eventoService.getAll();
		listaSemestres = new ArrayList<>();
		listaTutores = new ArrayList<>();
		listaIdTutores2 = new ArrayList<Long>();
		listaIdTutores = new ArrayList<Long>();
		
		refreshListaEventos();
	}

	// Se ejecuta cuando se presiona el botón Guardar en EventoDialogRegistrar
	public void registrarEvento() {

		try {
			listaTutores = new ArrayList<>();
			Itr itr = itrService.get(idItrSeleccionado);
			eventoNuevo.setEstadoEvento(EstadoEvento.CORRIENTE);
			itrService.get(idItrSeleccionado);
			eventoNuevo.setItr(itr);
			List<Tutor> tutores = new ArrayList<>();

			Query query = em.createNativeQuery("SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'SEQ_ID_EVENTO'");
			
			List<BigDecimal> li = query.getResultList();
			
			for (BigDecimal bigDecimal : li) {
				eventoNuevo.setIdEvento(bigDecimal.longValueExact());
			}
			
			/*
			 * for (Evento evento : eventoService.getAll()) {
			 * eventoNuevo.setIdEvento(evento.getIdEvento()); }
			 */

			System.out.println(eventoNuevo.getIdEvento());

			
			for (Long idTut : listaIdTutores) {
				tutores.add(tutorService.buscarTutorPorId(idTut));
			}
			eventoNuevo.setListaTutores(tutores);

			if (validationError.validarErroresEvento_Capa_1(eventoNuevo)) {

				eventoService.save(eventoNuevo);

				refreshListaEventos();

				limpiarCampos();
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
						.add("PF('dialogRegistrarEvento').hide()");
				MessagesUtil.createInfoMessage("Registro exitoso", "Se ha registrado de forma satisfactoria el Evento");
			} else {
				MessagesUtil.createInfoMessage("Datos inválidos",
						"Algunos datos ingresados para el Evento no son válidos.");
			}
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Ajax.update("dialogs:dialogRegistrarEventoContent");
		Ajax.update("seccion_lista_eventos");
		Ajax.update("seccion_lista_eventos: growl");

	}

	/*
	 * public void cleanListaIdTutor() { listaIdTutores = new ArrayList<Long>();
	 * System.out.println("Se limpio la lista"); }
	 */

	public void modificarEvento() {
		try {
			if (validationError.validarErroresEvento_Capa_1(eventoSeleccionado)
					&& validationError.validarErroresEvento_Capa_2(eventoSeleccionado)) {

				// Buscar el evento existente por su id
				Evento eventoExistente = eventoService.get(eventoSeleccionado.getIdEvento());

				eventoExistente.setListaTutores(null);

				teService.delete(eventoExistente);

				for (Long id : listaIdTutores2) {
					listaTutores.add(tutorService.buscarTutorPorId(id));
				}

				eventoSeleccionado.setListaTutores(listaTutores);

				// Realizar las modificaciones necesarias en el evento existente

				eventoExistente.setTitulo(eventoSeleccionado.getTitulo());
				eventoExistente.setInformacion(eventoSeleccionado.getInformacion());
				eventoExistente.setTipoEvento(eventoSeleccionado.getTipoEvento());
				eventoExistente.setFechaInicio(eventoSeleccionado.getFecha_Hora_Inicio());
				eventoExistente.setFechaFin(eventoSeleccionado.getFecha_Hora_Final());
				eventoExistente.setModalidad(eventoSeleccionado.getModalidad());
				eventoExistente.setLocalizacion(eventoSeleccionado.getLocalizacion());
				eventoExistente.setCreditos(eventoSeleccionado.getCreditos());
				eventoExistente.setEstadoEvento(eventoSeleccionado.getEstadoEvento());
				eventoExistente.setItr(eventoSeleccionado.getItr());
				eventoExistente.setListaTutores(eventoSeleccionado.getListaTutores());

				// Guardar los cambios en el evento existente

				eventoService.update(eventoService.eventoToDTO(eventoExistente));

				listaIdTutores2 = new ArrayList<Long>();

				listaTutores = new ArrayList<>();

				limpiarCampos();
				refreshListaEventos();
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
						.add("PF('dialogModificarEvento').hide()");

				MessagesUtil.createInfoMessage("Modificación exitosa",
						"Se ha modificado de forma satisfactoria el Evento");
			}
			

		} catch (ServiciosException e) {

			MessagesUtil.createInfoMessage("Datos inválidos",
					"Algunos datos ingresados para el Evento no son válidos.");
		}

		Ajax.update("dialogs:dialogModificarEventoContent");
		Ajax.update("seccion_lista_eventos");
		Ajax.update("seccion_lista_eventos: growl");
	}

	public void seleccionarEvento(Evento evento) {
		listaIdTutores2 = new ArrayList<Long>();
		Evento eve = null;

		for (Evento e : eventoService.getAll()) {
			if (evento.getIdEvento() == e.getIdEvento()) {
				eve = e;
			}
		}

		for (Tutor t : eve.getListaTutores()) {
			listaIdTutores2.add(t.getIdUsuario());
		}

		eventoSeleccionado = eventoService.eventoToDTO(eve);

		idItrSeleccionado = eventoSeleccionado.getItr().getIdItr();
		
		
		
		System.out.println(eventoSeleccionado.getListaTutores());

		Ajax.update("form:dialogModificarEventoContent");
	}

	public void eliminarEvento(long id) throws ServiciosException {
	
		eventoService.delete(id);
		
		refreshListaEventos();
		
	}

	public void limpiarCampos() {
		eventoSeleccionado = EventoDTO.builder().build();
		eventoNuevo = EventoDTO.builder().build();
	}


	public String cargarColumnaSemestre(List<Semestre> semestre) {
		String se = "";
		for (Semestre s : semestre) {
			se += s.getNombre().toString() + " ";
		}
		return se;
	}

	public String cargarColumnaTutores(Long idEvento) {
		String tu = "";
		List<Tutor> t = new ArrayList<>();

		for (Evento e : eventoService.getAll()) {
			if (e.getIdEvento() == idEvento) {
				t = e.getListaTutores();
				break;
			}

		}

		for (Tutor tutor : t) {
			tu += tutor.getNombre() + " " + tutor.getApellido() + " ";
		}
		return tu;
	}

	public boolean globalFilterFunctionEvento(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		Evento evento = (Evento) value;
		boolean estadoCondition = estadoFilter == null || evento.getEstadoEvento().equals(estadoFilter);

		boolean modalidadCondition = modalidadFilter == null || evento.getModalidad().equals(modalidadFilter);

		if (filterTextCondition) {
			boolean idCondition = (evento.getIdEvento() + "").toLowerCase().contains(filterText);
			boolean eventoCondition = evento.getTitulo().toLowerCase().contains(filterText);
			 
		
			return estadoCondition && modalidadCondition && (idCondition || eventoCondition);
		} else {
			return estadoCondition && modalidadCondition;
		}
	}

	public List<Itr> cargarListaItr() {
		List<Itr> itrActivados = new LinkedList<>();
		for (Itr itr : itrService.getAll()) {
			if (EstadoItr.ACTIVADO.equals(itr.getEstadoItr())) {
				itrActivados.add(itr);
			}

		}
		return itrActivados;
	}

	public List<Tutor> cargarListaTutores() {
		List<Tutor> tutorEncargado = new LinkedList<>();
		for (Tutor t : tutorService.obtenerTodosTutor()) {
			if (RolTutor.ENCARGADO.equals(t.getRolTutor())) {
				tutorEncargado.add(t);
			}

		}
		return tutorEncargado;
	}

	public void refreshListaEventos() {
		listaEventos = eventoService.getList(tipoUsuario, idUsuario);
		Ajax.update("seccion_lista_eventos");
	}

	public void refreshListaPorEstado(EstadoEvento estado) {
		listaEventos = new LinkedList<>();
		if (estado != null) {
			for (Evento eve : eventoService.getAll()) {
				if (eve.getEstadoEvento().equals(estado)) {
					listaEventos.add(eve);
				}
			}
		} else {
			listaEventos = eventoService.getAll();
		}
		Ajax.update("dataTableEvento");
	}

	public void refreshListaPorModalidad(Modalidad modalidad) {
		listaEventos = new LinkedList<>();
		if (modalidad != null) {
			for (Evento eve : eventoService.getAll()) {
				if (eve.getModalidad().equals(modalidad)) {
					listaEventos.add(eve);
				}
			}
		} else {
			listaEventos = eventoService.getAll();
		}
		Ajax.update("dataTableEvento");
	}
	
	public void refreshListaPorFiltro() {
		listaEventos = new LinkedList<>();
		
		if(modalidadFilter == null && estadoFilter == null) {
			listaEventos = eventoService.getAll();
		}else {
			listaEventos = eventoService.getByFilter(estadoFilter, modalidadFilter);
		}
	}

}


