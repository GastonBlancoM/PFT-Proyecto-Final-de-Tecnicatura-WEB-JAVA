package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.TutorDAO;
import com.dto.TutorDTO;
import com.dto.UsuarioDTO;
import com.entities.Tutor;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class TutorService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private TutorDAO tutorDAO;
	
	public String registrarTutor(UsuarioDTO usuDTO, TutorDTO tutDTO) {
		try {
			Tutor tutorNuevo = dtoToTutor(usuDTO,tutDTO);
			tutorDAO.crear(tutorNuevo);

			return "";

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String modificarTutor(UsuarioDTO usuDTO, TutorDTO tutDTO) {
		try {
			Tutor tutor = dtoToTutor(usuDTO,tutDTO);
			tutorDAO.modificar(tutor);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public List<Tutor> obtenerTodosTutor (){
		return tutorDAO.obtenerTodos();
	}
	public Tutor buscarTutorPorId (long id) {
		return tutorDAO.buscarTutor(id);
	}
	
	public Tutor dtoToTutor (UsuarioDTO usuDTO, TutorDTO tutDTO) {
		Tutor tutor = new Tutor();
		
		tutor.setIdUsuario(usuDTO.getIdUsuario());
		tutor.setNombre(usuDTO.getNombre());
		tutor.setApellido(usuDTO.getApellido());
		tutor.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		tutor.setFechaNacimiento(usuDTO.getFechaNacimiento());
		tutor.setNombreUsuario(usuDTO.getNombreUsuario());
		tutor.setContrasenia(usuDTO.getContrasenia());
		tutor.setTipoUsuario(usuDTO.getTipoUsuario());
		tutor.setVerificacion(usuDTO.getVerificacion());
		tutor.setMail(usuDTO.getMail());
		tutor.setMailInstitucional(usuDTO.getMailInstitucional());
		tutor.setTelefono(usuDTO.getTelefono());
		tutor.setGenero(usuDTO.getGenero());
		tutor.setEstadoUsuario(usuDTO.getEstadoUsuario());
		tutor.setItr(usuDTO.getItr());
		tutor.setLocalidad(usuDTO.getLocalidad());
		
		tutor.setArea(tutDTO.getArea());
		tutor.setRolTutor(tutDTO.getRolTutor());

		return tutor;
	}
	
	public TutorDTO tutorToDTO(Tutor t) {
		TutorDTO dto = new TutorDTO();
		dto.setArea(t.getArea());
		dto.setRolTutor(t.getRolTutor());
		return dto;
	}
}
