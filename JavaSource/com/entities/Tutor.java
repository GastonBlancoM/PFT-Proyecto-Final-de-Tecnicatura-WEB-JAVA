package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.enums.RolTutor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tutor")
@PrimaryKeyJoinColumn(referencedColumnName="IDUSUARIO")

public class Tutor extends Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String Area;
	
	@Column(name = "TIPOROL")
	@Enumerated(EnumType.STRING)
	private RolTutor rolTutor;
	
	
	
}

