package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.dao.Tutor_EventoDAO;
import com.dto.EventoDTO;
import com.entities.Convocatoria;
import com.entities.Estudiante;
import com.entities.Evento;
import com.entities.Justificacion;
import com.entities.Reclamo;
import com.entities.Tutor;
import com.entities.Tutor_Evento;
import com.enums.EstadoEvento;
import com.enums.Modalidad;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryEvento;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class EventoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private RepositoryEvento eventoDAO;

	@Inject
	private ItrService itrService;
	@Inject
	private TutorService tutorService;
	@Inject
	JustificacionService juService;
	@Inject
	ConvocatoriaService coService;
	@Inject
	ReclamoService reService;
	@Inject
	Tutor_EventoService teService;

	public Evento save(EventoDTO DTO) throws ServiciosException {
		Evento eventoNuevo = dtoToEvento(DTO);
		eventoDAO.save(eventoNuevo);
		return eventoNuevo;
	}

	public Evento update(EventoDTO DTO) throws ServiciosException {
		System.out.println("update service");
		Evento eventoNuevo = dtoToEvento(DTO);
		eventoDAO.update(eventoNuevo);
		return eventoNuevo;
	}

	
	public void delete(long idEvento) throws ServiciosException {
		
		List<Reclamo> r = reService.findByEvento(idEvento);
		List<Convocatoria> c = coService.findByEvento(idEvento);
		List<Justificacion> j = juService.findByEvento(idEvento);
		Evento ev = get(idEvento);
		
		try {
			if (r.isEmpty() && c.isEmpty() && j.isEmpty() && ev.getListaTutores().size()<=0 ) {
				eventoDAO.delete(get(idEvento));
				MessagesUtil.createInfoNotification("Evento eliminado", "Se ha eliminado el evento con exito.");
			} else {
				eventoDAO.deleteLogical(get(idEvento));
				MessagesUtil.createInfoMessage("Baja lógica aplicada",
						"Se han encontrado relaciones involucradas con este evento");
			}

		} catch (Exception e) {
			throw new ServiciosException(e.getMessage());
		}

	}

	public List<Evento> getAll() {
		return eventoDAO.getAll();
	}

	public Evento get(long id) {
		return eventoDAO.get(id);
	}

	public List<Evento> getEventosConTutores() {
		return eventoDAO.getEventosConTutores();
	}

	public List<Evento> getListByTutor(long idTutor) {
		Tutor tutor = tutorService.buscarTutorPorId(idTutor);
		return eventoDAO.getByTutor(tutor);
	}

	public List<Evento> getList(TipoUsuario tipo, long idUsuario) {
		return TipoUsuario.ANALISTA.equals(tipo) ? getAll() : getListByTutor(idUsuario);
	}

	public List<Evento> getByFilter(EstadoEvento estado, Modalidad modalidad) {
		return eventoDAO.getByFiltro(estado, modalidad);
	}

	public Evento dtoToEvento(EventoDTO DTO) {
		System.out.println("DTO evento");
		Evento evento = new Evento();
		evento.setIdEvento(DTO.getIdEvento());
		evento.setTitulo(DTO.getTitulo());
		evento.setTipoEvento(DTO.getTipoEvento());
		evento.setFechaInicio(DTO.getFecha_Hora_Inicio());
		evento.setFechaFin(DTO.getFecha_Hora_Final());
		evento.setModalidad(DTO.getModalidad());
		evento.setLocalizacion(DTO.getLocalizacion());
		evento.setCreditos(DTO.getCreditos());
		evento.setEstadoEvento(DTO.getEstadoEvento());
		evento.setInformacion(DTO.getInformacion());
		evento.setItr(DTO.getItr());
		evento.setListaTutores(DTO.getListaTutores());
		/* evento.setListaSemestres(DTO.getListaSemestres()); */

		return evento;
	}

	public EventoDTO eventoToDTO(Evento evento) {
		return EventoDTO.builder().idEvento(evento.getIdEvento()).Titulo(evento.getTitulo())
				.tipoEvento(evento.getTipoEvento()).fecha_Hora_Inicio(evento.getFechaInicio())
				.fecha_Hora_Final(evento.getFechaFin()).modalidad(evento.getModalidad())
				.localizacion(evento.getLocalizacion()).creditos(evento.getCreditos())
				.estadoEvento(evento.getEstadoEvento()).informacion(evento.getInformacion()).itr(evento.getItr())
				.listaTutores(evento.getListaTutores())
				/* .listaSemestres(evento.getListaSemestres()) */
				.build();

	}

}
