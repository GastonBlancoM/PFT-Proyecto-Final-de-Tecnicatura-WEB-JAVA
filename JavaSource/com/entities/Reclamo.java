package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.enums.EstadoReclamo;
import com.enums.TipoReclamo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reclamo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private long idReclamo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private TipoReclamo tipoReclamo;
	
	@Column(nullable = true)
	@JoinColumn(name = "TITULO")
	private String titulo;
	
	@Column(nullable = true, length = 100)
	private String detalle;

	@Column(nullable = true, length = 30)
	private Date fecha_Hora;

	@ManyToOne
	@JoinColumn(name = "SEMESTRE")
	private Semestre semestre;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private EstadoReclamo estadoReclamo;
	
	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;
	
	
}
