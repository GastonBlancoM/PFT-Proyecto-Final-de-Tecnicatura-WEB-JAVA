package com.client;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Departamento;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Localidad;
import com.entities.Usuario;
import com.enums.EstadoEstudiante;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.enums.RolTutor;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.exceptions.ServiciosException;
import com.services.AnalistaService;
import com.services.DepartamentoService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.MessagesUtil;

import lombok.Data;

@SessionScoped
@Named(value = "gestionListaUsuarios")
@Data
public class GestionListaUsuarios implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject	private ItrService itrService;
	@Inject	private TutorService tutorService;
	@Inject private UsuarioService usuarioService;
	@Inject	private AnalistaService analistaService;
	@Inject	private LocalidadService localidadService;
	@Inject	private EstudianteService estudianteService;
	@Inject private DepartamentoService departamentoService;
	
	private GestionUsuario gestionUsuario = new GestionUsuario();

	private boolean vistaTutor = false;
	private boolean vistaEstudiante = false;
	
	private List<Usuario> listaUsuarios = new LinkedList<>();
	private List<Localidad> listaLocalidades;
	
	private Usuario usuarioSeleccionado = new Usuario();
	private UsuarioDTO usuarioNuevo = UsuarioDTO.builder().build();
	private EstudianteDTO estudianteNuevo = new EstudianteDTO();
	private TutorDTO tutorNuevo = new TutorDTO();

	
	private String nombreDepartamentoSeleccionado;
	private String nombreRolTutorSeleccionado;
	
	private EstadoUsuario estadoFilter;
	private int genFilter;
	
	// VARIABLES PARA DIALOG MODIFICAR USUARIO
	private String estadoUsuarioSeleccionado;
	private String verificacionSeleccionado;
	private long idSeleccionada;
	private long idItrSeleccionado;
	private long idDepartamentoSeleccionado;
	private long idLocalidadSeleccionada;
	private boolean inputsVisMod;
	
	private com.utils.ValidationError validationError = new com.utils.ValidationError();

	// VARIABLES PARA DIALOG REGISTRAR USUARIO
	private String tipoUsuarioNuevo;
	private String nombreRolTutorNuevo;
	
	private LocalDate fechaActual = LocalDate.now();
	private String yearRange = (fechaActual.getYear() - 100)
			+ ":" +
			(fechaActual.getYear() + 10);
	
	private boolean renderInputEstudiante;
	private boolean renderInputTutor;
	private boolean renderInputText;
	private boolean verComboLocalidades = false;
	
	@PostConstruct
	public void init() {
		listaUsuarios = usuarioService.obtenerTodosUsuarios();
		usuarioNuevo.setItr(new Itr());
		usuarioNuevo.setDepartamento(new Departamento());
		usuarioNuevo.setLocalidad(new Localidad());
	}
	
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		Usuario usu = (Usuario) value;
		boolean estadoCondition = estadoFilter == null || usu.getEstadoUsuario().equals(estadoFilter);
		
		if (filterTextCondition) {
			boolean idCondition = (usu.getIdUsuario() + "").toLowerCase().contains(filterText);
			boolean nombreCondition = usu.getNombre().toLowerCase().contains(filterText);
			boolean apellidoCondition = usu.getApellido().toLowerCase().contains(filterText);
			boolean documentoCondition = (usu.getDocumento() + "").toLowerCase().contains(filterText);
			boolean itrCondition = usu.getItr().getNombre().toLowerCase().contains(filterText);
			boolean tipoCondition = usu.getTipoUsuario().getNombre().toLowerCase().contains(filterText);
			boolean verificacionCondition = usu.getVerificacion().getNombre().toLowerCase().contains(filterText);
			return estadoCondition && (idCondition || itrCondition || nombreCondition || apellidoCondition || documentoCondition || tipoCondition || verificacionCondition);
		} else {
			return estadoCondition;
		}
	}
	
	public void borrarUsuario(long id) throws ServiciosException {
		usuarioService.borrarUsuarioLogicamente(id);
		cargarListarUsuarios();
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
	
	public String anioParaCalendario() {
		LocalDate fechaActual = LocalDate.now();
		int a = fechaActual.getYear() - 100;
		int b = fechaActual.getYear() + 10;
		return a + ":" + b;
	}
	
	public String cargarListarUsuarios() {
		listaUsuarios = usuarioService.obtenerTodosUsuarios();
		//return "/analista/mainContent_Lista_De_Usuarios.xhtml?faces-redirect=true";
		return null;
	}

	/*
	 * FUNCIONES PARA DIALOG MODIFICAR USUARIO
	 * Las siguientes funciones son para dar funcionalidad al Modificar usuario dentro
	 * de el Listado de Usuarios al que puede acceder el Analista
	 */
	
	// Se ejecuta al presionar en el botón de modificar usuario en el listado.
	public void seleccionarUsuario() {
		idItrSeleccionado = usuarioSeleccionado.getItr().getIdItr();
		idDepartamentoSeleccionado = usuarioSeleccionado.getLocalidad().getDepartamento().getIdDepartamento();
		idLocalidadSeleccionada = usuarioSeleccionado.getLocalidad().getIdLocalidad();
		
		estudianteNuevo = new EstudianteDTO();
		tutorNuevo = new TutorDTO();
		usuarioNuevo.setIdUsuario(usuarioSeleccionado.getIdUsuario());
		usuarioNuevo.setNombre(usuarioSeleccionado.getNombre());
		usuarioNuevo.setApellido(usuarioSeleccionado.getApellido());
		usuarioNuevo.setDocumento(usuarioSeleccionado.getDocumento() + "");
		usuarioNuevo.setFechaNacimiento(usuarioSeleccionado.getFechaNacimiento());
		usuarioNuevo.setMail(usuarioSeleccionado.getMail());
		usuarioNuevo.setMailInstitucional(usuarioSeleccionado.getMailInstitucional());
		usuarioNuevo.setTelefono(usuarioSeleccionado.getTelefono());
		usuarioNuevo.setTipoUsuario(usuarioSeleccionado.getTipoUsuario());
		usuarioNuevo.setVerificacion(usuarioSeleccionado.getVerificacion());
		usuarioNuevo.setItr(usuarioSeleccionado.getItr());
		usuarioNuevo.setNombreUsuario(usuarioSeleccionado.getNombreUsuario());
		usuarioNuevo.setContrasenia(usuarioSeleccionado.getContrasenia());
		usuarioNuevo.setEstadoUsuario(usuarioSeleccionado.getEstadoUsuario());
		usuarioNuevo.setLocalidad(usuarioSeleccionado.getLocalidad());
		usuarioNuevo.setGenero(usuarioSeleccionado.getGenero());
		usuarioNuevo.setDepartamento(usuarioNuevo.getLocalidad().getDepartamento());
		
		vistaEstudiante = usuarioSeleccionado.getTipoUsuario() == TipoUsuario.ESTUDIANTE;
		vistaTutor = usuarioSeleccionado.getTipoUsuario() == TipoUsuario.TUTOR;
		
		if(vistaEstudiante) {
			Estudiante estudiante = estudianteService.buscarEstudiantePorId(usuarioSeleccionado.getIdUsuario());
			System.out.println(estudiante.getAñoIngreso());
			System.out.println(estudiante.getEstadoEstudiante());
			estudianteNuevo.setAñoIngreso(estudiante.getAñoIngreso());
			estudianteNuevo.setEstadoEstudiante(estudiante.getEstadoEstudiante());
		}
		if (vistaTutor) {
			tutorNuevo.setArea(tutorService.buscarTutorPorId(usuarioSeleccionado.getIdUsuario()).getArea());
			tutorNuevo.setRolTutor(tutorService.buscarTutorPorId(usuarioSeleccionado.getIdUsuario()).getRolTutor());
		}
		cargarListaLocalidades();
	}
	
	// Se ejecuta al presionar el botón de guardar en el dialogo de modificar usuario.
	public void modificarUsuario() {
		usuarioNuevo.setItr(itrService.get(idItrSeleccionado));
		usuarioNuevo.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		usuarioNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		if(validationError.validarErroresUsuario(usuarioNuevo, estudianteNuevo, tutorNuevo, usuarioService)) {
			if(vistaEstudiante) {
				estudianteService.modificarEstudiante(usuarioNuevo, estudianteNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoNotification("Modificación exitosa", "Los datos del estudiante fueron modificados correctamente.");
				limpiarCampos();
			}else if(vistaTutor) {
				tutorService.modificarTutor(usuarioNuevo, tutorNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoNotification("Modificación exitosa", "Los datos del tutor fueron modificados correctamente.");
				limpiarCampos();
			}else {
				analistaService.modificarAnalista(usuarioNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogModificarUsuario').hide()");
				MessagesUtil.createInfoMessage("Modificación exitosa", "Los datos del analista fueron modificados correctamente.");
				limpiarCampos();
			}
			
			cargarListarUsuarios();
		}

	}
	
	
	public void limpiarCampos() {
		usuarioNuevo = new UsuarioDTO();
		estudianteNuevo = new EstudianteDTO();
		tutorNuevo = new TutorDTO();
		idItrSeleccionado = 0;
		idDepartamentoSeleccionado = 0;
		idLocalidadSeleccionada = 0;
		
	}
	
	public void menuItemChanged() {
		renderInputText = "Estudiante".equals(tipoUsuarioNuevo); 
	}
	
	public void registrarUsuario() {
		
//		TipoUsuario tipoAsignado =
//				TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioNuevo) ? TipoUsuario.ANALISTA :
//				TipoUsuario.TUTOR.getNombre().equals(tipoUsuarioNuevo) ? TipoUsuario.TUTOR :
//				TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioNuevo) ? TipoUsuario.ESTUDIANTE :
//					null;
//		
//		usuarioNuevo.setTipoUsuario(tipoAsignado);
		usuarioNuevo.setItr(itrService.get(idItrSeleccionado));
		usuarioNuevo.setVerificacion(Verificacion.NO_VERIFICADO);
		usuarioNuevo.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuarioNuevo.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		usuarioNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		estudianteNuevo.setEstadoEstudiante(EstadoEstudiante.NO_MATRICULADO);
		
		if(usuarioNuevo.getMailInstitucional().contains("@")) {
			String[] partes = usuarioNuevo.getMailInstitucional().split("@");
			usuarioNuevo.setNombreUsuario(partes[0]);
		}
		
		RolTutor rolAsignado = RolTutor.ENCARGADO.getNombre().equals(nombreRolTutorNuevo)
				? RolTutor.ENCARGADO
				: RolTutor.TUTOR.getNombre().equals(nombreRolTutorNuevo)
				? RolTutor.TUTOR
				: null;
		tutorNuevo.setRolTutor(rolAsignado);
		
		System.out.println(usuarioSeleccionado);
		System.out.println(tutorNuevo);
		if(validationError.validarErroresUsuario(usuarioNuevo, estudianteNuevo, tutorNuevo, usuarioService)) {
			if(TipoUsuario.ESTUDIANTE.equals(usuarioNuevo.getTipoUsuario())) {

				estudianteService.registrarEstudiante(usuarioNuevo, estudianteNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
				
			}else if(TipoUsuario.TUTOR.equals(usuarioNuevo.getTipoUsuario())) {
				
				tutorService.registrarTutor(usuarioNuevo, tutorNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
			}else {

				analistaService.registrarAnalista(usuarioNuevo);
				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarUsuario').hide()");
			}
			
		}
		
	}
		
	public List<Departamento> cargarListaDepartamento(){
		return departamentoService.obtenerTodosDepartamento();
	}
	
	public void refreshListaUsuarios() {
		listaUsuarios = usuarioService.obtenerTodosUsuarios();
		Ajax.update("seccion_lista_usuarios");
	}
	
	public void refreshListaUsuarios(EstadoUsuario estado) {
		listaUsuarios = new LinkedList<>();
		if (estado != null) {
			for (Usuario usu : usuarioService.obtenerTodosUsuarios()) {
				if (usu.getEstadoUsuario().equals(estado)) {
					listaUsuarios.add(usu);
				}
			}
		}else {
			listaUsuarios = usuarioService.obtenerTodosUsuarios();
		}
		Ajax.update("usuarios");
	}
	
	public void cargarListaLocalidades() {
		listaLocalidades = localidadService.obtenerLocalidadesPorDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		
		if (listaLocalidades.size() > 0) {	
			verComboLocalidades = true;
		} else {
			listaLocalidades = new ArrayList<>();
			verComboLocalidades = false;
		}
		
			
	}
	
	public List<Integer> cargarListaAños() {
		List<Integer> listaAños = new LinkedList<>();

		for (int year = fechaActual.getYear() - 30; year <= fechaActual.getYear() + 10; year++) {
			listaAños.add(year);
        }
		
		return listaAños;
		
	}
	
	public void menuItemChanged(String tipoUsuario) {
		if ("ESTUDIANTE".equals(tipoUsuario)) {
			renderInputEstudiante = true;
			renderInputTutor = false;


		} else if ("TUTOR".equals(tipoUsuario)) {
			renderInputEstudiante = false;
			renderInputTutor = true;

		} else {
			renderInputEstudiante = false;
			renderInputTutor = false;
		}

	}
	
	public void filtrarPorGen() {
		listaUsuarios = new LinkedList<>();
		if (genFilter != -1) {
			for (Usuario u : usuarioService.obtenerTodosUsuarios()) {
				if (u.getTipoUsuario().equals(TipoUsuario.ESTUDIANTE)) {
					Estudiante est = estudianteService.buscarEstudiantePorId(u.getIdUsuario());
					if (est.getAñoIngreso() == genFilter) {
						listaUsuarios.add(u);
					}
				}
			}
		}else {
			listaUsuarios = usuarioService.obtenerTodosUsuarios();
		}
		Ajax.update("usuarios");
	}
}
