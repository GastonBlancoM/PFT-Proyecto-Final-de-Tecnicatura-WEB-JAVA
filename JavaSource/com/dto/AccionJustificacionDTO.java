package com.dto;

import java.util.Date;

import com.entities.Analista;
import com.entities.Justificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccionJustificacionDTO {
	
	private long idAccion;
	
	private String detalle;
	
	private Date fechaHora;
	
	private Analista analista;
	
	private Justificacion justificacion;
	
}
