package com.dto;

import com.entities.Departamento;
import com.entities.Localidad;
import com.enums.EstadoItr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItrDTO {
	private long idItr;
	
	private String nombre;
	
	private Localidad localidad;
	
	private Departamento departamento;

	private EstadoItr estado;
	
}
