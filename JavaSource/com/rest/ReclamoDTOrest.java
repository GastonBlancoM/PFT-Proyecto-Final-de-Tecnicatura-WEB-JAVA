package com.rest;

import java.util.Date;

import com.enums.EstadoReclamo;
import com.enums.TipoReclamo;

import lombok.Data;

@Data
public class ReclamoDTOrest {
	private long idReclamo;
	
	private String titulo;

	private TipoReclamo tipoReclamo;

	private String detalle;

	private Date fecha_Hora;

	private long idSemestre;
	
	private EstadoReclamo estadoReclamo;
	
	private long idEstudiante;

	private long idEvento;
	
}
