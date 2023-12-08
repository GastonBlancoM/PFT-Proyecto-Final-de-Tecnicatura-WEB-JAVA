package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.omnifaces.util.Ajax;

import com.dto.ConvocatoriaDTO;
import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryConvocatoria;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class ConvocatoriaService implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = RepositoryConvocatoria.class, beanName="ConvocatoriaDAO")
	private RepositoryConvocatoria<Convocatoria> dao;

	@Inject private EventoService eventoService;
	@Inject private EstudianteService estudianteService;
	
	
	public Convocatoria save(ConvocatoriaDTO DTO) throws ServiciosException {
		Convocatoria newConvocatoria = convertDtoToConvocatoria(DTO);
		
			dao.save(newConvocatoria);
			MessagesUtil.createInfoMessage("Convocatoria exitosa", "La operación fue completada con normalidad.");
			return newConvocatoria;
	
	
	}
	
	public Convocatoria delete(ConvocatoriaDTO DTO) throws ServiciosException{
		Convocatoria deleteConvocado = convertDtoToConvocatoria(DTO);
		dao.delete(deleteConvocado);
		return deleteConvocado;
	}
	
	public Convocatoria update(ConvocatoriaDTO DTO) throws ServiciosException{
		Convocatoria updateConvocado = convertDtoToConvocatoria(DTO);
		dao.update(updateConvocado);
		return updateConvocado;
	}
	
	
	public Convocatoria getByIdConvocatoria(Long id) {
		return dao.get(id);
	}
	
	public List<Convocatoria> getAll() {
		return dao.getAll();
	}
	
	public List<Convocatoria> findByEstudiante(long idEstudiante){
		Estudiante estudiante = estudianteService.buscarEstudiantePorId(idEstudiante);
		return dao.getByEstudiante(estudiante);
	}
	
	public List<Convocatoria> findByEvento(long idEvento){
		Evento evento = eventoService.get(idEvento);
		return dao.getByEvento(evento);
	}
	
	public List<Convocatoria> obtenerPorFiltros(Estudiante estudiante){
		return dao.getByFilter(estudiante);
	}
	
	public List<Convocatoria> getFilterByEstudentEvent(Estudiante estudiante, Evento evento){
		return dao.getFilterByEstudentEvent(estudiante, evento);
	}
	
	public Convocatoria convertDtoToConvocatoria(ConvocatoriaDTO dto) {
		return Convocatoria.builder()
				.idConvocatoria(dto.getIdConvocatoria())
				.calificacion(dto.getCalificacion())
				.tipoAsistencia(dto.getTipoAsistencia())
				.evento(dto.getEvento())
				.estudiante(dto.getEstudiante())
				.build();
	}
	
	public ConvocatoriaDTO convertConvocatoriaToDto(Convocatoria convocatoria) {
		return ConvocatoriaDTO.builder()
				.idConvocatoria(convocatoria.getIdConvocatoria())
				.calificacion(convocatoria.getCalificacion())
				.tipoAsistencia(convocatoria.getTipoAsistencia())
				.evento(convocatoria.getEvento())
				.estudiante(convocatoria.getEstudiante())
				.build();
	}

	public List<Convocatoria> devFindAll() {
		return dao.getAll();
	}
	
	public List<Convocatoria> getList(TipoUsuario tipo, Estudiante estudiante){
		System.out.println(tipo);
		return TipoUsuario.ANALISTA.equals(tipo)
				? devFindAll()
				: obtenerPorFiltros(estudiante);
	}
	
}
