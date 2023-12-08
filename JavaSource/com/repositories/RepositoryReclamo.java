package com.repositories;

import java.util.List;

import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Reclamo;

public interface RepositoryReclamo extends Repository<Reclamo>{
	List<Reclamo> getByEstudiante(Estudiante estudiante);
	
	List<Reclamo> getByEvento(Evento evento);
}
