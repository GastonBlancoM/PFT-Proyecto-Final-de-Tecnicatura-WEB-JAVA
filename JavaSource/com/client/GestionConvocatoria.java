package com.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.ConvocatoriaDTO;
import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Usuario;
import com.enums.TipoAsistencia;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.ConvocatoriaService;
import com.services.EstudianteService;
import com.services.EventoService;
import com.services.UsuarioService;
import com.utils.NavegacionController;

import lombok.Data;

@ViewScoped
@Named(value = "gestionConvocatoria")
@Data
public class GestionConvocatoria implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Convocatoria> listaConvocatoria;

	private NavegacionController render;

	private TipoUsuario tipoUsuario;
	private long idUsuario;

	private ConvocatoriaDTO newConvocatoria = new ConvocatoriaDTO();

	private Evento eventoSeleccionado = new Evento();
	
	private Convocatoria convocatoriaSeleccionada = new Convocatoria();

	@Inject
	private GestionAutenticacion autenticacion;
	@Inject
	private GestionEvento gestionEvento;
	/* @Inject private JustificacionManager gestionListaJustificacion; */

	@Inject
	private EstudianteService estudianteService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private ConvocatoriaService convocatoriaService;
	@Inject
	private EventoService eventoService;

	private List<Long> listaIdEstudiantes;
	private List<Estudiante> listaEstudiantes;
	private long idEstudianteSeleccionado;

	@PostConstruct
	public void init() {

		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		render = new NavegacionController();
		

		refreshListaConvocatoria();
	}

	public boolean globalFilterFunctionJustificacion(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Convocatoria convocatoria = (Convocatoria) value;
		return (convocatoria.getIdConvocatoria() + "").toLowerCase().contains(filterText)
				|| convocatoria.getEvento().getTitulo().toLowerCase().contains(filterText)
				|| convocatoria.getTipoAsistencia().getNombre().toLowerCase().contains(filterText);
	}

	public void createConvocatoria() {
		try {
			Convocatoria conv;
			for (Long id : listaIdEstudiantes) {
				Estudiante est = estudianteService.buscarEstudiantePorId(id);
				newConvocatoria.setEvento(eventoSeleccionado);
				newConvocatoria.setEstudiante(est);
				newConvocatoria.setTipoAsistencia(TipoAsistencia.ASISTENCIA);
				newConvocatoria.setCalificacion(1f);

				conv = convocatoriaService.save(newConvocatoria);
				listaConvocatoria.add(conv);
			}
			limpiarCampos();

		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			limpiarCampos();
		}

	}

	public void deleteConvocado(Long id) {
		ConvocatoriaDTO convDTO = convocatoriaService
				.convertConvocatoriaToDto(convocatoriaService.getByIdConvocatoria(id));
		try {
			convocatoriaService.delete(convDTO);
			refreshListaConvocatoria();
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateConvocatoria() {
		ConvocatoriaDTO convSeleccionada = convocatoriaService.convertConvocatoriaToDto(convocatoriaSeleccionada);
		
		try {
			convocatoriaService.update(convSeleccionada);
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarConvocatoria').hide()");
			
			refreshListaConvocatoria();
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void limpiarCampos() {
		eventoSeleccionado = new Evento();
	}

	public void seleccionarConvocatoria(Convocatoria convocatoria) {
		Convocatoria conv = null;
		
		for (Convocatoria c : convocatoriaService.getAll()) {
			if(convocatoria.getIdConvocatoria() == c.getIdConvocatoria()) {
				conv=c;
			}
		}
		
		convocatoriaSeleccionada=conv;
		
		Ajax.update("form:dialogModificarConvocatoriaContent");
	}
	
	public void seleccionarEvento(Evento evento) {
		Evento eve = null;

		for (Evento e : eventoService.getAll()) {
			if (evento.getIdEvento() == e.getIdEvento()) {
				eve = e;
			}
		}

		eventoSeleccionado = eve;

		FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
				.add("PF('dialogConvocarEstudiantes').hide()");
		Ajax.update("form:dialogConvocarEstudiantesContent");
	}

	public List<Usuario> cargarListaEstudiantes() {
		List<Usuario> estudiante = new LinkedList<>();
		for (Estudiante e : estudianteService.obtenerTodosEstudiantes()) {
			if(convocatoriaService.getFilterByEstudentEvent(e, eventoSeleccionado).isEmpty()) {
				estudiante.add(e);
			}

		}
		return estudiante;
	}

	public void onRefresh() {
		refreshListaConvocatoria();
	}

	private void refreshListaConvocatoria() {
		listaConvocatoria  = convocatoriaService.getList(tipoUsuario,
				estudianteService.buscarEstudiantePorId(idUsuario));
		
	
	}

	public long getTotalCount(String titulo) {
		return listaConvocatoria.stream()
				.filter(listaConvocatoria -> titulo.equals(listaConvocatoria.getEvento().getTitulo())).count();
	}
}
