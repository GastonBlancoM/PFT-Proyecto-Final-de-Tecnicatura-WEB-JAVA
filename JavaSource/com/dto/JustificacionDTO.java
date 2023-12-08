package com.dto;

import java.util.Date;
import java.util.List;

import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.EstadoJustificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JustificacionDTO {
	
	private long idJustificacion;
	
	private String detalle;
	
	private Date fechaHora;
	
	private EstadoJustificacion estadoJustificacion;
	
	private Estudiante estudiante;
	
	private Evento evento;
	
	private List<AccionJustificacionDTO> accion;
}
