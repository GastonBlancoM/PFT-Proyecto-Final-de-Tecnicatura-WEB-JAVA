package com.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dto.AccionJustificacionDTO;
import com.dto.JustificacionDTO;
import com.entities.AccionJustificacion;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.enums.EstadoJustificacion;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryJustificacion;
import com.utils.FieldValidationException;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class JustificacionService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB private RepositoryJustificacion dao;
	
	@Inject private EventoService eventoService;
	@Inject private AnalistaService analistaService;
	@Inject private EstudianteService estudianteService;
	
	public Justificacion save(JustificacionDTO dto) throws ServiciosException {
		Justificacion j = convertDtoToJustificacion(dto);
		j.setFechaHora(new Date());
		j.setEstudiante(dto.getEstudiante());
		j.setEstadoJustificacion(EstadoJustificacion.INGRESADO);
		dao.save(j);
		return j;
		
	}
	
	public Justificacion update(JustificacionDTO dto) throws ServiciosException {
		Justificacion j = dao.get(dto.getIdJustificacion());
		j.setFechaHora(new Date());
		j.setDetalle(dto.getDetalle());
		j.setEstadoJustificacion(dto.getEstadoJustificacion());
		dao.update(j);
		return j;
		
	}
	
	public void delete(long idJustificacion) throws ServiciosException {
		Justificacion j = dao.get(idJustificacion);
		if(j != null) {
			if(!j.getAccion().isEmpty())throw new ServiciosException("No puedes eliminar esta justificación, un analistas ya tomó acciones sobre la misma.");
			
			dao.delete(j);
		}
	}
	
	public void actionCreate(AccionJustificacionDTO accionDto, JustificacionDTO justdto, long idAnalista) throws FieldValidationException, ServiciosException {
		Justificacion justificacion = findById(justdto.getIdJustificacion());
		AccionJustificacion accion = convertDtoToAccionJustificacion(accionDto);
		accion.setFechaHora(new Date());
		accion.setJustificacion(justificacion);
		accion.setAnalista(analistaService.buscarAnalistaPorId(idAnalista));
		justificacion.getAccion().add(accion);
		dao.accionCreate(accion, justificacion);
	}
	
	public Justificacion findById(long idJustificacion) {
		return dao.get(idJustificacion);
	}
	
	public List<Justificacion> findAll() {
		return dao.getAll();
	}
	
	public List<Justificacion> findByEstudiante(long idEstudiante) {
		Estudiante estudiante = estudianteService.buscarEstudiantePorId(idEstudiante);
		return dao.getByEstudiante(estudiante);
	}
	
	public List<Justificacion> findByEvento(long idEvento) {
		Evento evento = eventoService.get(idEvento);
		return dao.getByEvento(evento);
	}
	
	
	public List<Justificacion> getList(TipoUsuario tipo, long idUsuario){
		return TipoUsuario.ANALISTA.equals(tipo)
				? findAll()
				: findByEstudiante(idUsuario);
	}
	
	public Justificacion convertDtoToJustificacion(JustificacionDTO dto) {
		return Justificacion.builder()
				.idJustificacion(dto.getIdJustificacion())
				.detalle(dto.getDetalle())
				.fechaHora(dto.getFechaHora())
				.estadoJustificacion(dto.getEstadoJustificacion())
				.estudiante(dto.getEstudiante())
				.evento(dto.getEvento())
				.build();
	}
	
	public JustificacionDTO convertJustificacionToDto(Justificacion justificacion) {
		return JustificacionDTO.builder()
				.idJustificacion(justificacion.getIdJustificacion())
				.detalle(justificacion.getDetalle())
				.fechaHora(justificacion.getFechaHora())
				.estadoJustificacion(justificacion.getEstadoJustificacion())
				.estudiante(justificacion.getEstudiante())
				.evento(justificacion.getEvento())
				.build();
	}
	
	public AccionJustificacionDTO convertAccionJustificacionToDto(AccionJustificacion accion) {
		return AccionJustificacionDTO.builder()
				.idAccion(accion.getIdAccion())
				.detalle(accion.getDetalle())
				.fechaHora(accion.getFechaHora())
				.analista(accion.getAnalista())
				.justificacion(accion.getJustificacion())
				.build();
	}
	
	public AccionJustificacion convertDtoToAccionJustificacion(AccionJustificacionDTO dto) {
		return AccionJustificacion.builder()
				.idAccion(dto.getIdAccion())
				.detalle(dto.getDetalle())
				.fechaHora(dto.getFechaHora())
				.analista(dto.getAnalista())
				.justificacion(dto.getJustificacion())
				.build();
	}
	
}
