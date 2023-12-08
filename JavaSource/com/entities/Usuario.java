package com.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="Usuario")
public class Usuario {
	@Id
	private long idUsuario;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "APELLIDO")
	private String apellido;
	
	@Column(name = "DOCUMENTO")
	private int documento;
	
	@Column(name = "FECHANACIMIENTO")
	private Date fechaNacimiento;
	
	@Column(name = "NOMBREUSUARIO")
	private String nombreUsuario;
	
	@Column(name = "CONTRASENIA")
	private String contrasenia;
	
	@Column(name = "TIPOUSUARIO")
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipoUsuario;

	@Column(name = "VERIFICACION")
	@Enumerated(EnumType.STRING)
	private Verificacion verificacion;
	
	@Column(name = "MAIL")
	private String mail;
	
	@Column(name = "MAIL_INSTITUCIONAL")
	private String mailInstitucional;
	
	@Column(name = "TELEFONO")
	private String telefono;
	
	@Column(name = "GENERO")
	private String genero;
	
	@Column(name = "ESTADO")
	@Enumerated(EnumType.STRING)
	private EstadoUsuario estadoUsuario;
	//cambiar en todos los registros y agregar esto del estado
	
	@ManyToOne
	@JoinColumn(name = "IDITR")
	private Itr itr;
	
	@ManyToOne
	@JoinColumn(name = "IDLOCALIDAD")
	private Localidad localidad;//
}
