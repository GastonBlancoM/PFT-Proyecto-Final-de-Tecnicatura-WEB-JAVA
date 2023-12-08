package com.repositories;

import java.util.List;

import com.entities.Estudiante;
import com.entities.Evento;
import com.exceptions.ServiciosException;

public interface RepositoryConvocatoria<C> {

	C get(long id);

	List<C> getAll();

	List<C> getByEstudiante(Estudiante estudiante);

	List<C> getByEvento(Evento evento);

	List<C> getByFilter(Estudiante estudiante);
	
	List<C> getFilterByEstudentEvent(Estudiante estudiante, Evento evento);

	void save(C e) throws ServiciosException;

	void update(C e) throws ServiciosException;

	void delete(C e) throws ServiciosException;

}
