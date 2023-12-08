package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.AnalistaDAO;
import com.dto.UsuarioDTO;
import com.entities.Analista;
import com.repositories.Repository;
import com.utils.MessagesUtil;

import lombok.Data;
import java.io.Serializable;


//Este es un Service
@Data
@Stateless
@LocalBean
public class AnalistaService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="AnalistaDAO")
	private Repository<Analista> analistaDAO;
	
	public String registrarAnalista(UsuarioDTO usuDTO) {
		try {
			Analista analistaNuevo = dtoToAnalista(usuDTO);
			analistaDAO.save(analistaNuevo);
			MessagesUtil.createInfoMessage("Registro de analista correcto", "El analista se registro correctamente, este usuario debe ser verificado para poder logearse.");
			return "";

		} catch (Exception e) {

			MessagesUtil.createErrorMessage(e.getMessage(), "El analista no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");

		} finally {

		}
		return "";
	}
	public String modificarAnalista(UsuarioDTO usuDTO) {
		try {
			Analista analista = dtoToAnalista(usuDTO);
			analistaDAO.update(analista);
			MessagesUtil.createInfoMessage("Usuario analista modificado", "El usuario fue modificado correctamente.");
		} catch (Exception e) {

			MessagesUtil.createErrorMessage(e.getMessage(), "Asegúrate de que los datos ingresados del analista son correctos.");

		} 
		return "";
	}
	public List<Analista> obtenerTodosAnalista (){
		return analistaDAO.getAll();
	}
	public Analista buscarAnalistaPorId (long id) {
		return analistaDAO.get(id);
	}
	
	public Analista dtoToAnalista (UsuarioDTO usuDTO) {
		Analista analista = new Analista();
		
		analista.setIdUsuario(usuDTO.getIdUsuario());
		analista.setNombre(usuDTO.getNombre());
		analista.setApellido(usuDTO.getApellido());
		analista.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		analista.setFechaNacimiento(usuDTO.getFechaNacimiento());
		analista.setNombreUsuario(usuDTO.getNombreUsuario());
		analista.setContrasenia(usuDTO.getContrasenia());
		analista.setTipoUsuario(usuDTO.getTipoUsuario());
		analista.setVerificacion(usuDTO.getVerificacion());
		analista.setMail(usuDTO.getMail());
		analista.setMailInstitucional(usuDTO.getMailInstitucional());
		analista.setTelefono(usuDTO.getTelefono());
		analista.setGenero(usuDTO.getGenero());
		analista.setEstadoUsuario(usuDTO.getEstadoUsuario());
		analista.setItr(usuDTO.getItr());
		analista.setLocalidad(usuDTO.getLocalidad());

		return analista;
	}
	
	
}
