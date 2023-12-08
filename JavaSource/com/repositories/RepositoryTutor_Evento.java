package com.repositories;

import java.util.List;

import com.entities.Evento;
import com.entities.Tutor_Evento;
import com.exceptions.ServiciosException;

public interface RepositoryTutor_Evento extends Repository<Tutor_Evento>{

	List<Tutor_Evento> findByEvento(Evento evento);
	
	void delete(Evento t) throws ServiciosException;
	
}
