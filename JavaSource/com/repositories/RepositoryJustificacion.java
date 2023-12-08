package com.repositories;

import java.util.List;

import com.entities.AccionJustificacion;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.exceptions.ServiciosException;

public interface RepositoryJustificacion extends Repository<Justificacion> {

	List<Justificacion> getByEstudiante(Estudiante estudiante);

	List<Justificacion> getByEvento(Evento evento);

	List<AccionJustificacion> getAccionByJustificacion(Justificacion justificacion);

	AccionJustificacion accionCreate(AccionJustificacion accion, Justificacion justificacion) throws ServiciosException;

	

	
}
