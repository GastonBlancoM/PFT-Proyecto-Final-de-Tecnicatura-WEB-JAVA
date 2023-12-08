package com.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SEMESTRES_EVENTOS")
public class Semestre_Evento {

	@Id
	/* @JoinColumn(name = "ID") */
	private long id;

	@ManyToOne
	@JoinColumn(name = "IDSEMESTRE")
	private Semestre idSemestre;

	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento idEvento;

}
