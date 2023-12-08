package com.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.enums.EstadoEvento;
import com.enums.Modalidad;
import com.enums.TipoEvento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "EVENTO")
public class Evento {

	@Id
	private long idEvento;

	@Column(name = "TITULO", nullable = true, length = 30)//
	private String titulo;

	@Column(name = "INFORMACION", nullable = true, length = 30)//
	private String informacion;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPOEVENTO", nullable = true)//
	private TipoEvento tipoEvento;

	@Column(name = "FECHA_INICIO", nullable = true, length = 30)//
	private Date fechaInicio;

	@Column(name = "FECHA_FIN", nullable = true, length = 30)//
	private Date fechaFin;

	@Enumerated(EnumType.STRING)
	@Column(name = "MODALIDAD", nullable = true)//
	private Modalidad modalidad;

	@Column(name = "LOCALIZACION", nullable = true, length = 100)//
	private String localizacion;

	@Column(name = "CREDITOS", nullable = false, length = 30)//
	private int creditos;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADOEVENTO", nullable = true)
	private EstadoEvento estadoEvento;

	@ManyToOne
	@JoinColumn(name = "IDITR")//
	private Itr itr;
	
	@ManyToMany(fetch = FetchType.EAGER)//Se guardan en listaTutores
	@JoinTable(name = "TUTORES_EVENTOS", joinColumns = @JoinColumn(name = "IDEVENTO", referencedColumnName = "idEvento"), inverseJoinColumns = @JoinColumn(name = "IDUSUARIO"))
	private List<Tutor> listaTutores;

	public String toStringSinEntidades() {
		return "Evento [idEvento=" + idEvento + ", titulo=" + titulo + ", informacion=" + informacion + ", tipoEvento="
				+ tipoEvento + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", modalidad=" + modalidad
				+ ", localizacion=" + localizacion + ", creditos=" + creditos + ", estadoEvento=" + estadoEvento + "]";
	}
}

