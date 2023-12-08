package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "ACCION_JUSTIFICACION")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccionJustificacion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IDACC_JUS")
	private long idAccion;
	
	@Column(name = "FECHAHORA", nullable = false, length = 20)
	private Date fechaHora;
	
	@Column(name = "DETALLE", nullable = false, length = 100)
	private String detalle;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "IDANALISTA")
	private Analista analista;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "IDJUSTIFICACION", nullable = false)
	private Justificacion justificacion;
}
