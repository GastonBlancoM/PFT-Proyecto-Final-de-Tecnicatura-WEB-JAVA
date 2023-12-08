package com.dto;

import com.enums.EstadoEstudiante;

import lombok.Data;

@Data
public class EstudianteDTO {
	
	private EstadoEstudiante estadoEstudiante;
	
	private int añoIngreso;
	
}
