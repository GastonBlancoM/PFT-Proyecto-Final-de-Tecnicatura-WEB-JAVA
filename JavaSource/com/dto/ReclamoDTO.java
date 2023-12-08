package com.dto;

import java.util.Date;


import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Semestre;
import com.enums.EstadoReclamo;
import com.enums.TipoReclamo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamoDTO {
private long idReclamo;
	
	private String titulo;

	private TipoReclamo tipoReclamo;

	private String detalle;

	private Date fecha_Hora;

	private Semestre semestre;
	
	private EstadoReclamo estadoReclamo;
	
	private Estudiante estudiante;

	private Evento evento;
}
