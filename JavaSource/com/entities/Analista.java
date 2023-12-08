package com.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Analista")
@PrimaryKeyJoinColumn(referencedColumnName="IDUSUARIO")
public class Analista extends Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
}
