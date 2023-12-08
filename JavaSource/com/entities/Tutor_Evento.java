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
@Table(name = "TUTORES_EVENTOS")
public class Tutor_Evento {

	@Id 
	private long id;
	 
   
	@ManyToOne
	@JoinColumn(name = "IDUSUARIO")
	private Tutor idTutor;

 
	@ManyToOne
	@JoinColumn(name = "IDEVENTO")
	private Evento idEvento;

    
    
}



