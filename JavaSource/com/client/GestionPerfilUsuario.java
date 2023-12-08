package com.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;

import com.dto.EstudianteDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Estudiante;
import com.entities.Tutor;
import com.entities.Usuario;
import com.enums.TipoUsuario;
import com.services.AnalistaService;
import com.services.EstudianteService;
import com.services.ItrService;
import com.services.TutorService;
import com.services.UsuarioService;
import com.utils.NavegacionController;

import lombok.Data;

@SessionScoped
@Named(value = "gestionPerfilUsuario")
@Data
public class GestionPerfilUsuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TipoUsuario tipoUsuario;	
	private long idUsuario;				
	private NavegacionController render = new NavegacionController();
	
	private long idItrSeleccionado;
	
	
private com.utils.ValidationError validationError = new com.utils.ValidationError();
	
	@Inject	private AnalistaService analistaService;
	@Inject	private TutorService tutorService;
	@Inject	private EstudianteService estudianteService;
	@Inject	private UsuarioService usuarioService;
	@Inject	private ItrService itrService;
	@Inject	private GestionAutenticacion autenticacion;
	
	private UsuarioDTO usuario = new UsuarioDTO();
	private EstudianteDTO estudiante = new EstudianteDTO();
	private TutorDTO tutor = new TutorDTO();
	private Usuario usuarioLogueado;
	private Estudiante estudianteLogueado;
	private Tutor tutorLogueado;
	
	@PostConstruct
	public void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		render.AlternVista(tipoUsuario, null);
		cargarDatosUsuario();
		
	}
	
	public void modificarDatosPropios() {
		
		if (usuario.getTipoUsuario().equals(TipoUsuario.ESTUDIANTE)) {
			Estudiante e = estudianteService.buscarEstudiantePorId(usuario.getIdUsuario());
			
			if (validationError.validarErroresUsuario(usuario, estudianteService.estudianteToDTO(e), null, usuarioService)) {
				estudianteService.modificarEstudiante(usuario, estudianteService.estudianteToDTO(e));
			}
		}else if (usuario.getTipoUsuario().equals(TipoUsuario.TUTOR)) {
			Tutor t = tutorService.buscarTutorPorId(usuario.getIdUsuario());
			
			if (validationError.validarErroresUsuario(usuario, null, tutorService.tutorToDTO(t), usuarioService)) {
				tutorService.modificarTutor(usuario, tutorService.tutorToDTO(t));
			}
		}else if (usuario.getTipoUsuario().equals(TipoUsuario.ANALISTA)) {
			analistaService.modificarAnalista(usuario);
			
		}
		cargarDatosUsuario();
		Ajax.update("seccion_datos");
		
	}
	
	public void cargarDatosUsuario() {
		usuarioLogueado = usuarioService.buscarUsuarioPorId(idUsuario);
		estudianteLogueado = estudianteService.buscarEstudiantePorId(idUsuario);
		tutorLogueado = tutorService.buscarTutorPorId(idUsuario);
		
		if (tutorLogueado != null) {
			tutor.setArea(tutorLogueado.getArea());
			tutor.setRolTutor(tutorLogueado.getRolTutor());
		}
		if (estudianteLogueado != null) {
			estudiante.setAñoIngreso(estudianteLogueado.getAñoIngreso());
			estudiante.setEstadoEstudiante(estudianteLogueado.getEstadoEstudiante());
		}
		
		usuario = usuarioService.convertUsuariotoDto(usuarioLogueado);
		usuario.setIdUsuario(usuarioLogueado.getIdUsuario());
		
	}
	
	public String convertirFechaNac(Date fechaNac) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fechaNac);
    }
	
}
