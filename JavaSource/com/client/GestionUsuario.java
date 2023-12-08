package com.client;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Departamento;
import com.entities.Estudiante;
import com.entities.Itr;
import com.entities.Localidad;
import com.enums.EstadoEstudiante;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.enums.RolTutor;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.services.AnalistaService;
import com.services.DepartamentoService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.MessagesUtil;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Named(value = "gestionUsuario")
@SessionScoped
@Data
public class GestionUsuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private UsuarioService usuarioService;
	@Inject private ItrService itrService;
	@Inject private LocalidadService localidadService;
	@Inject	private AnalistaService analistaService;
	@Inject	private EstudianteService estudianteService;
	@Inject	private TutorService tutorService;
	@Inject	private DepartamentoService departamentoService;

	private com.utils.ValidationError validationError = new com.utils.ValidationError();

	private UsuarioDTO usuarioSeleccionado = UsuarioDTO.builder().build();
	private EstudianteDTO estudianteSeleccionado = new EstudianteDTO();
	private TutorDTO tutorSeleccionado = new TutorDTO();
	
	private String nombreDepartamentoSeleccionado;
	private String nombreRolTutorSeleccionado;
	private long idLocalidadSeleccionada;
	private Long idItr;
	private long idDepartamentoSeleccionado;
	private String nombreLocalidadSeleccionada;
	private String tipoUsuarioSeleccionado;
	private long idItrSeleccionado;
	
	private boolean renderInputEstudiante;
	private boolean renderInputTutor;
	private boolean renderInputText;
	
	private boolean verComboLocalidades = false;
	
	List<Localidad> listaLocalidades = new LinkedList<>();
	List<String> listaNombreLocalidades;
	
//	private String idMenuCarreras;
//	private String idMenuUsuarios;
//	private String idMenuItrs;
//	private String idMenuPerfil;
//	private String idMenuPrincipal;
	
	private int pruebaAño;
	
	private LocalDate fechaActual = LocalDate.now();
	private String yearRange = (fechaActual.getYear() - 100)
			+ ":" +
			(fechaActual.getYear() + 10);
	
	
	
	
	@PostConstruct
	public void init() {
		usuarioSeleccionado = UsuarioDTO.builder().build();
		estudianteSeleccionado = new EstudianteDTO();
		tutorSeleccionado = new TutorDTO();
		
		idItrSeleccionado = 0;
		idDepartamentoSeleccionado = 0;
		idLocalidadSeleccionada = 0;
	}

	public void registrarUsuario() {
		TipoUsuario tipoAsignado =
				TipoUsuario.ANALISTA.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.ANALISTA :
				TipoUsuario.TUTOR.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.TUTOR :
				TipoUsuario.ESTUDIANTE.getNombre().equals(tipoUsuarioSeleccionado) ? TipoUsuario.ESTUDIANTE :
					null;
		
		usuarioSeleccionado.setTipoUsuario(tipoAsignado);
		usuarioSeleccionado.setItr(itrService.get(idItrSeleccionado));
		usuarioSeleccionado.setVerificacion(Verificacion.NO_VERIFICADO);
		usuarioSeleccionado.setEstadoUsuario(EstadoUsuario.ACTIVO);
		usuarioSeleccionado.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDepartamentoSeleccionado));
		estudianteSeleccionado.setEstadoEstudiante(EstadoEstudiante.MATRICULADO);
		usuarioSeleccionado.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSeleccionada));
		
		if(usuarioSeleccionado.getMailInstitucional().contains("@")) {
			String[] partes = usuarioSeleccionado.getMailInstitucional().split("@");
			usuarioSeleccionado.setNombreUsuario(partes[0]);
		}
		
		RolTutor rolAsignado = RolTutor.ENCARGADO.getNombre().equals(nombreRolTutorSeleccionado)
				? RolTutor.ENCARGADO
				: RolTutor.TUTOR.getNombre().equals(nombreRolTutorSeleccionado)
				? RolTutor.TUTOR
				: null;
		tutorSeleccionado.setRolTutor(rolAsignado);
		
		if(validationError.validarErroresUsuario(usuarioSeleccionado, estudianteSeleccionado, tutorSeleccionado, usuarioService)) {
			if(TipoUsuario.ESTUDIANTE.equals(usuarioSeleccionado.getTipoUsuario())) {

				estudianteService.registrarEstudiante(usuarioSeleccionado, estudianteSeleccionado);
				MessagesUtil.createInfoNotification("Registro de estudiante correcto", "El estudiante se registró correctamente, este usuario debe ser verificado para poder logearse.");
			}else if(TipoUsuario.TUTOR.equals(usuarioSeleccionado.getTipoUsuario())) {
				
				tutorService.registrarTutor(usuarioSeleccionado, tutorSeleccionado);
				MessagesUtil.createInfoNotification("Registro de tutor correcto", "Se registró correctamente el usuario Tutor, debe ser verificado para poder logearse.");
				
			}else {
				analistaService.registrarAnalista(usuarioSeleccionado);
				MessagesUtil.createInfoMessage("Registro de analista correcto", "El analista se registró correctamente, este usuario debe ser verificado para poder logearse.");
				
			}
		}
		limpiarCampos();
	}

	public void limpiarCampos() {
		usuarioSeleccionado = UsuarioDTO.builder().build();
		estudianteSeleccionado = new EstudianteDTO();
		tutorSeleccionado = new TutorDTO();
		
		idItrSeleccionado = 0;
		idDepartamentoSeleccionado = 0;
		idLocalidadSeleccionada = 0;
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
	
	public List<Departamento> cargarListaDepartamento(){
		return departamentoService.obtenerTodosDepartamento();
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

	public void menuItemChanged(String tipoUsuario) {
		if ("Estudiante".equals(tipoUsuario)) {
			renderInputEstudiante = true;
			renderInputTutor = false;


		} else if ("Tutor".equals(tipoUsuario)) {
			renderInputEstudiante = false;
			renderInputTutor = true;

		} else {
			renderInputEstudiante = false;
			renderInputTutor = false;
			nombreRolTutorSeleccionado = null;
		}

	}
	
	public List<Integer> cargarListaAños() {
		List<Integer> listaAños = new LinkedList<>();

		for (int year = fechaActual.getYear() - 30; year <= fechaActual.getYear() + 10; year++) {
			listaAños.add(year);
        }
		
		return listaAños;
		
	}
	
	public void showMessage(FacesMessage message) {
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void infoMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	public List<Integer> cargarFiltroGen() {
		List<Estudiante> listaEstudiantes = estudianteService.obtenerTodosEstudiantes();
		List<Integer> listaGeneraciones = new LinkedList<>();
		
		for (Estudiante est : listaEstudiantes) {
			if (!(listaGeneraciones.contains(est.getAñoIngreso()))) {
				listaGeneraciones.add(est.getAñoIngreso());
			}
			
		}
		
		return listaGeneraciones;
	}
	
}
