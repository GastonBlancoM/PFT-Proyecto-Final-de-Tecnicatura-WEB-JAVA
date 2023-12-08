package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.entities.Evento;
import com.entities.Tutor_Evento;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryTutor_Evento;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class Tutor_EventoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private RepositoryTutor_Evento dao;

	@Inject
	private EventoService eventoService;
	
	public void delete(Evento t) throws ServiciosException{
		dao.delete(t);
	}

	public List<Tutor_Evento> findByEvento(long idEvento) {
		Evento evento = eventoService.get(idEvento);
		return dao.findByEvento(evento);
	}
}
