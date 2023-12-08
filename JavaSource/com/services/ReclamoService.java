package com.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Reclamo;
import com.entities.Semestre;
import com.repositories.RepositoryReclamo;
import com.rest.ReclamoDTOrest;

import lombok.Data;


@Data
@Stateless
@LocalBean
public class ReclamoService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private SemestreService semestreService;
	@Inject private AnalistaService analistaService;
	@Inject private EventoService eventoService;
	@Inject private EstudianteService estudianteService;
	
	@EJB private RepositoryReclamo dao;
	
	public Reclamo dtoRestToReclamo(ReclamoDTOrest reclamoDtoRest) {
		Semestre semestre = semestreService.buscarPorId(reclamoDtoRest.getIdSemestre());
		Evento evento = eventoService.get(reclamoDtoRest.getIdEvento());
		Estudiante estudiante = estudianteService.buscarEstudiantePorId(reclamoDtoRest.getIdEstudiante());
		
		Reclamo reclamo = new Reclamo();
		reclamo.setIdReclamo(reclamoDtoRest.getIdReclamo());
		reclamo.setTitulo(reclamoDtoRest.getTitulo());
		reclamo.setTipoReclamo(reclamoDtoRest.getTipoReclamo());
		reclamo.setDetalle(reclamoDtoRest.getDetalle());
		reclamo.setFecha_Hora(new Date());
		reclamo.setSemestre(semestre);
		reclamo.setEstadoReclamo(reclamoDtoRest.getEstadoReclamo());
		reclamo.setEstudiante(estudiante);
		reclamo.setEvento(evento);
		
		return reclamo;
	}
	
	
	public List<Reclamo> findByEvento(long idEvento){
		Evento evento = eventoService.get(idEvento);
		return dao.getByEvento(evento);
	}
	
	public Reclamo get(long idReclamo) {
		return dao.get(idReclamo);
	}
}
