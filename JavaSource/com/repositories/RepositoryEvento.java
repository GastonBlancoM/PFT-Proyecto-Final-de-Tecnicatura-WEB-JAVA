package com.repositories;

import java.util.List;

import com.entities.Evento;
import com.entities.Tutor;
import com.enums.EstadoEvento;
import com.enums.Modalidad;
import com.exceptions.ServiciosException;

public interface RepositoryEvento extends Repository<Evento>{
	
	
	List<Evento> getEventosConTutores();
	
	List<Evento> getByFiltro(EstadoEvento estado, Modalidad modalidad);
	
	List<Evento> getByTutor(Tutor tutor);

	void deleteLogical(Evento evento) throws ServiciosException;
	
}
