package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.enums.EstadoEstudiante;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Estudiante")
@PrimaryKeyJoinColumn(referencedColumnName="IDUSUARIO")
public class Estudiante extends Usuario implements Serializable {
	private static final long serialVersionUID = 5385063536424646834L;

	@Column(name = "ESTADOESTUDIANTE")
	@Enumerated(EnumType.STRING)
	private EstadoEstudiante estadoEstudiante;
	
	private int añoIngreso;
}
