package com.dto;

import java.util.Date;

import com.entities.Departamento;
import com.entities.Itr;
import com.entities.Localidad;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
private long idUsuario;
	
	private String nombre;
	
	private String apellido;
	
	private String documento;
	
	private Date fechaNacimiento;
	
	private String nombreUsuario;
	
	private String contrasenia;

	private TipoUsuario tipoUsuario;

	private Verificacion verificacion;
	
	private String mail;
	
	private String mailInstitucional;
	
	private String telefono;
	
	private String genero;

	private EstadoUsuario estadoUsuario;

	private Itr itr;

	private Localidad localidad;
	
	private Departamento departamento;
}	

