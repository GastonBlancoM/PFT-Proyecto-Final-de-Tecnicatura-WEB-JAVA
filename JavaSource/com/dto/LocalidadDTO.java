package com.dto;

import com.entities.Departamento;

import lombok.Data;

@Data
public class LocalidadDTO {
	private long idLocalidad;
	
	private String nombre;
	
	private Departamento departamento;

}
