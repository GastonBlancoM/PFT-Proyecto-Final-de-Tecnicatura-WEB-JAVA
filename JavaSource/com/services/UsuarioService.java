package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;


import com.dto.UsuarioDTO;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryUsuario;
import com.utils.CookiesUtil;
import com.utils.JwtUtil;
import com.utils.ValidationError;

import lombok.Data;
import java.io.Serializable;

@Data
@Stateless
@LocalBean
public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private RepositoryUsuario dao;

	public List<Usuario> obtenerTodosUsuarios() {
		return dao.getAll();
	}

	public void borrarUsuarioLogicamente(long id) throws ServiciosException {
		dao.deleteLogical(id);
	}

	public Usuario buscarUsuarioPorId(long id) {
		return dao.get(id);
	}

	public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
		return dao.getByNombreUsuario(nombreUsuario);
	}

	public Usuario obtenerUsuarioPorDocumento(String documento) {
		return dao.getByDocumento(Integer.parseInt(documento));
	}
	
	public Usuario getUsuarioByMailInstitucional(String mail) {
		return dao.getByMailInstitucional(mail);
	}
	
	public Usuario login(String nombreUsuario, String password) {
		Usuario usuario = dao.getByNombreUsuario(nombreUsuario);
		
		if (ValidationError.validarLogin(usuario, nombreUsuario, password)) {
			String token = JwtUtil.createJwt(nombreUsuario, usuario.getTipoUsuario(), usuario.getIdUsuario());
			CookiesUtil.setCookie("jwt", token, 480 * 60);
			return usuario;
		} else {
			return null;
		}
	}
	
	public UsuarioDTO convertUsuariotoDto(Usuario usuario) {
		return UsuarioDTO.builder().nombre(usuario.getNombre()).apellido(usuario.getApellido())
				.documento(usuario.getDocumento() + "").fechaNacimiento(usuario.getFechaNacimiento())
				.nombreUsuario(usuario.getNombreUsuario()).contrasenia(usuario.getContrasenia())
				.tipoUsuario(usuario.getTipoUsuario()).verificacion(usuario.getVerificacion()).mail(usuario.getMail())
				.mailInstitucional(usuario.getMailInstitucional()).telefono(usuario.getTelefono())
				.genero(usuario.getGenero()).estadoUsuario(usuario.getEstadoUsuario()).itr(usuario.getItr())
				.localidad(usuario.getLocalidad()).departamento(usuario.getLocalidad().getDepartamento()).build();
	}

	public Usuario convertDtotoUsuario(UsuarioDTO usuario) {
		return Usuario.builder().nombre(usuario.getNombre()).apellido(usuario.getApellido())
				.documento(Integer.parseInt(usuario.getDocumento())).fechaNacimiento(usuario.getFechaNacimiento())
				.nombreUsuario(usuario.getNombreUsuario()).contrasenia(usuario.getContrasenia())
				.tipoUsuario(usuario.getTipoUsuario()).verificacion(usuario.getVerificacion()).mail(usuario.getMail())
				.mailInstitucional(usuario.getMailInstitucional()).telefono(usuario.getTelefono())
				.genero(usuario.getGenero()).estadoUsuario(usuario.getEstadoUsuario()).itr(usuario.getItr())
				.localidad(usuario.getLocalidad()).build();
	}

}