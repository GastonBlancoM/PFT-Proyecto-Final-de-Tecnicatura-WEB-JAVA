package com.dto;

import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.TipoAsistencia;

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
public class ConvocatoriaDTO {
	private long idConvocatoria;

	private float calificacion;

	private TipoAsistencia tipoAsistencia;

	private Evento evento;

	private Estudiante estudiante;
}
