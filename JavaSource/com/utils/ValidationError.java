package com.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.dto.EstudianteDTO;
import com.dto.EventoDTO;
import com.dto.ItrDTO;
import com.dto.ReclamoDTO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.services.UsuarioService;

public class ValidationError {
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~])[A-Za-z\\d!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~]{8,}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

	LocalDate fechaActual = LocalDate.now();
	
	public static boolean validarLogin(Usuario usuario, String nombreUsuarioLogin, String contraseniaLogin) {
		if (usuario == null) {
			MessagesUtil.createWarnMessage("Usuario no válido", "El usuario ingresado no existe en el sistema.");
			return false;
		} else if (!nombreUsuarioLogin.equals(usuario.getNombreUsuario()) || !contraseniaLogin.equals(usuario.getContrasenia())) {
			MessagesUtil.createWarnMessage("Credenciales no válidas", "Los datos ingresados no son válidos. Acceso denegado.");
			return false;
		} else if (Verificacion.VERIFICADO != usuario.getVerificacion()) {
			MessagesUtil.createWarnMessage("Usuario no autorizado", "El usuario todavia no esta verificado, espere a que un Analista lo verifique");
			return false;
		}else if (EstadoUsuario.ACTIVO != usuario.getEstadoUsuario()) {
			MessagesUtil.createWarnMessage("Usuario no esta Activo", "El usuario no tiene el estado Activo, no puede ingresar a la aplicación");
			return false;
		}
		return true;
	}
	
	public boolean validarErroresUsuario(UsuarioDTO usu, EstudianteDTO est, TutorDTO tut, UsuarioService usuarioService) {

		if (usu.getNombre().isEmpty() || !(usu.getNombre().length() >= 1 && usu.getNombre().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El nombre debe contener entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getApellido().isEmpty() || !(usu.getApellido().length() >= 1 && usu.getApellido().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El apellido debe contener entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getDocumento().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El documento no puede estar vacío."));

			return false;
		}
		if (usu.getDocumento().length() >= 9) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El documento no puede tener mas de 8 caracteres."));

			return false;
		}
		if (!verificarCI(usu.getDocumento())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El documento no es válido."));

			return false;
		}
		List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();

		for (Usuario usuario : usuarios) {
			if (usu.getIdUsuario() == usuario.getIdUsuario()
					&& Integer.parseInt(usu.getDocumento()) == usuario.getDocumento()) {
				break;
			} else if (Integer.parseInt(usu.getDocumento()) == usuario.getDocumento()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error!", "Ya existe un usuario con ese documento."));
				return false;
			}
		}
		if ((usu.getFechaNacimiento() == null)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La fecha de nacimiento no puede estar vacía."));
			return false;
		}

		if (usu.getFechaNacimiento() != null) {
			LocalDate fechaNacimiento = usu.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			Period periodo = Period.between(fechaNacimiento, fechaActual);
			int edad = periodo.getYears();

			if (!fechaActual.isAfter(fechaNacimiento)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error!", "La fecha de nacimiento no puede ser mayor a la fecha actual."));

				return false;
			}
			if (edad < 18) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error!", "Segun la fecha de nacimiento registrada, no eres mayor a 18 años."));

				return false;
			}
		}

		if (!(verificarCorreoElectronico(usu.getMail()) || !(usu.getMail().length() >= 1 && usu.getMail().length() <= 320))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El mail debe contener un @, uno de los siguientes dominios: gmail.com,yahoo.com,hotmail.com,outlook.com y entre 1 y 320 dígitos."));

			return false;
		}

		if ((!usu.getMailInstitucional().contains("@") && !usu.getMailInstitucional().endsWith("utec.edu.uy"))
				|| !(usu.getMailInstitucional().length() >= 1 && usu.getMailInstitucional().length() <= 320)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El mail debe contener un @, el dominio utec.edu.uy y entre 1 y 320 dígitos."));

			return false;
		}

		if (usu.getTelefono().isEmpty() || !(usu.getTelefono().length() == 9)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El número de teléfono debe contener 9 dígitos."));
			return false;
		}
		
		if (usu.getGenero() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El genero no puede estar vacío."));

			return false;
		}

		if (usu.getTipoUsuario() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El tipo de usuario no puede estar vacío."));

			return false;
		}

		if (usu.getItr() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El Itr no puede estar vacío."));

			return false;
		}
		
		if (usu.getDepartamento() == null) {
            MessagesUtil.createErrorNotification("Error!", "El departamento no puede estar vacía.");
            return false;
        }
		
		if (usu.getLocalidad() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La localidad no puede estar vacía."));

			return false;
		}
		
		if (!PASSWORD_PATTERN.matcher(usu.getContrasenia()).matches()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La contraseña debe contener al menos una letra mayúscula, al menos un dígito y al menos un carácter especial (cualquier carácter de puntuación o símbolo). La longitud de la contraseña debe ser de al menos 8 caracteres."));
			return false;
		}
		
		if (usu.getEstadoUsuario() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El estado de usuario no puede estar vacío."));

			return false;
		}
		if (usu.getVerificacion() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La verificacion no puede estar vacío."));

			return false;
		}

		if (TipoUsuario.TUTOR.equals(usu.getTipoUsuario())) {
			if (tut.getArea() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
						"El área no debe estar vacía."));

				return false;
			}
			if (tut.getRolTutor() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
						"El rol no puede estar vacío."));

				return false;
			}
		}
		if(TipoUsuario.ESTUDIANTE.equals(usu.getTipoUsuario())) {
			if (est.getEstadoEstudiante() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
						"El estado del estudiante no puede estar vacío."));

				return false;
			}
			
			if (est.getAñoIngreso() == 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
						"El año de ingreso no puede estar vacío."));

				return false;
			}
		}

		return true;

	}
	
	private static boolean verificarCI(String ciSeleccionada) {
		String CI = ciSeleccionada;

		if (CI.length() == 7) {
			CI = "0" + CI;
		}

		int ultimoDigito = CI.charAt(CI.length() - 1);
		int digitoVerificar = Character.getNumericValue(ultimoDigito);

		String codigoMultiplicador = "2987634";
		int acumulador = 0;

		for (int i = 0; i < CI.length() - 1; i++) {
			acumulador += Character.getNumericValue(CI.charAt(i))
					* Character.getNumericValue(codigoMultiplicador.charAt(i));
		}

		int digitoVerificador = (10 - (acumulador % 10)) % 10;

		if (digitoVerificar == digitoVerificador) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean verificarCorreoElectronico(String mail) {
		// Expresión regular para verificar el formato del correo electrónico
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);

		if (!matcher.matches()) {
			return false; // El formato del correo electrónico no es válido
		}

		// Lista de dominios de correo electrónico comunes
		List<String> dominiosComunes = Arrays.asList("gmail.com", "yahoo.com", "hotmail.com", "outlook.com"
		// Agrega más dominios si es necesario
		);

		// Obtener el dominio del correo electrónico
		String[] partes = mail.split("@");
		if (partes.length != 2) {
			return false; // No se pudo obtener el dominio
		}

		String dominio = partes[1];

		// Verificar si el dominio pertenece a la lista de dominios comunes
		return dominiosComunes.contains(dominio.toLowerCase());
	}
	
	public boolean validarErroresItr(ItrDTO itr) {
        if (itr.getNombre() == null || !(itr.getNombre().length() >= 1 && itr.getNombre().length() <= 30)) {
            MessagesUtil.createErrorNotification("Error!", "El nombre debe contener entre 1 y 30 dígitos.");
            return false;
        }
        if (itr.getDepartamento() == null) {
            MessagesUtil.createErrorNotification("Error!", "El Departamento no puede estar vacío.");
            return false;
        }
        if (itr.getEstado() == null) {
            MessagesUtil.createErrorNotification("Error!", "El Estado no puede estar vacío.");
            return false;
        }
        return true;
    }
	
	public boolean validarErroresEvento_Capa_1(EventoDTO evento) {
        if (evento.getTitulo() == null || !(evento.getTitulo().length() >= 1 && evento.getTitulo().length() <= 50)) {
            MessagesUtil.createErrorNotification("Error!", "El nombre debe contener entre 1 y 50 dígitos.");
            return false;
        }
        if (evento.getTipoEvento() == null) {
            MessagesUtil.createErrorNotification("Error!", "El Tipo de evento no puede estar vacío.");
            return false;
        }
        
        if (evento.getModalidad() == null) {
            MessagesUtil.createErrorNotification("Error!", "La modalidad no puede estar vacía.");
            return false;
        }
        
        if (evento.getItr() == null) {
            MessagesUtil.createErrorNotification("Error!", "El ITR no puede estar vacío.");
            return false;
        }
        
        if (evento.getListaTutores().isEmpty()) {
            MessagesUtil.createErrorNotification("Error!", "Tienes que asignar al menos un tutor encargado.");
            return false;
        }
		
		if (evento.getLocalizacion() == null) {
            MessagesUtil.createErrorNotification("Error!", "La localización no puede estar vacía");
            return false;
        }
        
        if (evento.getFecha_Hora_Inicio() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La fecha de inicio no puede estar vacía."));
			return false;
		}
        
        if (evento.getFecha_Hora_Final() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La fecha de fin no puede estar vacía."));
			return false;
		}

		if (evento.getFecha_Hora_Final().before(evento.getFecha_Hora_Inicio())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La fecha de fin no puede ser anterior a la fecha de inicio."));
				return false;
		}
        
		if (evento.getCreditos() <= 0) {
            MessagesUtil.createErrorNotification("Error!", "El estado no puede estar vacío.");
            return false;
        }
		
        
		/*
		 * if (evento.getInformacion() == null) {
		 * MessagesUtil.createErrorNotification("Error!",
		 * "La información no puede estar vacía"); return false; }
		 */
		
        
        if (evento.getEstadoEvento() == null) {
            MessagesUtil.createErrorNotification("Error!", "El estado no puede estar vacío.");
            return false;
        }
        
        
        
       
        
        return true;
    }
	
	public boolean validarErroresEvento_Capa_2(EventoDTO evento) {
		
		
		 if (evento.getListaTutores().isEmpty()) {
	            MessagesUtil.createErrorNotification("Error!", "Tienes que asignar al menos un tutor encargado.");
	            return false;
	        }
		 
		 
			/*
			 * if (evento.getListaSemestres() == null) {
			 * MessagesUtil.createErrorNotification("Error!",
			 * "Tienes que asignar al menos un semestre."); return false; }
			 */
		return true;
	}
	
	public static void validarReclamo(ReclamoDTO dto) throws FieldValidationException {
        if (dto.getTitulo() == null || !(dto.getTitulo().length() >= 1 && dto.getTitulo().length() <= 50))
            throw new FieldValidationException("El título debe contener entre 1 y 50 caracteres.");
        
        if (dto.getDetalle() == null || !(dto.getDetalle().length() >= 1 && dto.getDetalle().length() <= 200))
        	throw new FieldValidationException("El detalle debe contener entre 1 y 200 caracteres.");

        if (dto.getEvento() == null)
        	throw new FieldValidationException("El Evento no puede estar vacío.");
        
        if (dto.getEstadoReclamo() == null)
        	throw new FieldValidationException("El Estado no puede estar vacío.");
        
        if (dto.getSemestre() == null)
        	throw new FieldValidationException("El Semestre no puede estar vacío.");
        
        if (dto.getTipoReclamo() == null)
        	throw new FieldValidationException("El Tipo de Reclamo no puede estar vacío.");
    }
	
}
