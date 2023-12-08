package com.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.enums.TipoAsistencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CONVOCATORIA_EVENTO")

public class Convocatoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDCON_EVE")
	private long idConvocatoria;

	@Column(name = "CALIFICACION", nullable = false, length = 30)
	private float calificacion;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOASISTENCIA", nullable = false)
	private TipoAsistencia tipoAsistencia;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento evento;

	@ManyToOne
	@JoinColumn(name = "IDESTUDIANTE")
	private Estudiante estudiante;
	
//	@ManyToMany(fetch = FetchType.EAGER)//Se guardan en listaEstudiantes
//	@JoinTable(name = "CONVOCATORIA_ESTUDIANTE", joinColumns = @JoinColumn(name = "IDCON_EVE", referencedColumnName = "idConvocatoria"), inverseJoinColumns = @JoinColumn(name = "IDUSUARIO"))
//	private List<Estudiante> listaEstudiantes;
}
