package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.enums.EstadoJustificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "JUSTIFICACION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Justificacion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private long idJustificacion;
	
	@Column(name = "DETALLE", nullable = false, length = 200)
	private String detalle;
	
	@Column(name = "FECHA_HORA", nullable = false, length = 20)
	private Date fechaHora;
	
	@Column(name = "ESTADOJUSTIFICACION")
	@Enumerated(EnumType.STRING)
	private EstadoJustificacion estadoJustificacion;
	
	@ManyToOne
	@JoinColumn(name= "IDESTUDIANTE")
	private Estudiante estudiante;
	
	@ManyToOne
	@JoinColumn(name= "IDEVENTO")
	private Evento evento;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "justificacion")
	private List<AccionJustificacion> accion;
}
