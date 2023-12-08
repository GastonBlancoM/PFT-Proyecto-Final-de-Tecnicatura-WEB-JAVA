package com.dto;

import java.util.Date;

import com.entities.Departamento;
import com.entities.Itr;
import com.entities.Localidad;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

public class AnalistaDTO extends UsuarioDTO{

	AnalistaDTO(long idUsuario, String nombre, String apellido, String documento, Date fechaNacimiento,
			String nombreUsuario, String contrasenia, TipoUsuario tipoUsuario, Verificacion verificacion, String mail,
			String mailInstitucional, String telefono, String genero, EstadoUsuario estadoUsuario, Itr itr,
			Localidad localidad, Departamento departamento) {
		super(idUsuario, nombre, apellido, documento, fechaNacimiento, nombreUsuario, contrasenia, tipoUsuario, verificacion,
				mail, mailInstitucional, telefono, genero, estadoUsuario, itr, localidad, departamento);
		// TODO Auto-generated constructor stub
	}

}
