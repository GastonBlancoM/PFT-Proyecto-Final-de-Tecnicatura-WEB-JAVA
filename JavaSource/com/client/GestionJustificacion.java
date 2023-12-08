package com.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

import com.dto.AccionJustificacionDTO;
import com.dto.ConvocatoriaDTO;
import com.dto.JustificacionDTO;
import com.entities.Convocatoria;
import com.entities.Justificacion;
import com.enums.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.ConvocatoriaService;
import com.services.JustificacionService;
import com.utils.FieldValidationException;
import com.utils.MessagesUtil;
import com.utils.NavegacionController;

import lombok.Data;

@Data
@ViewScoped
@Named(value = "gestionJustificacion")
public class GestionJustificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private long idUsuario;
	private TipoUsuario tipoUsuario;
	private NavegacionController render;
	private String estadoFilter;
	private Long idEstadoFilter;

	private List<Justificacion> listaJustificaciones;

	@Inject
	private GestionAutenticacion autenticacion;
	@Inject
	private ConvocatoriaService convocatoriaService;
	@Inject
	private JustificacionService justificacionService;

	private JustificacionDTO newJust;
	private JustificacionDTO selectJust;
	private ConvocatoriaDTO convocatoria;
	private AccionJustificacionDTO accionJust;
	private Convocatoria selectConvocatoria;

	@PostConstruct
	public void init() {
		idUsuario = autenticacion.loadIdUsuarioLogeado();
		tipoUsuario = autenticacion.loadTipoUsuarioLogeado();
		render = new NavegacionController();

		accionJust = new AccionJustificacionDTO();
		
		newJust = JustificacionDTO.builder().build();
		selectJust = JustificacionDTO.builder().build();

		rechargeListaJustificaciones();
	}

	
	public void selectJustificacion(Justificacion justificacion) {
		selectJust = justificacionService.convertJustificacionToDto(justificacion);
	}

	public void create() {
		try {
			convocatoria = convocatoriaService.convertConvocatoriaToDto(selectConvocatoria);
			newJust.setEstudiante(convocatoria.getEstudiante());
			newJust.setEvento(convocatoria.getEvento());
			justificacionService.save(newJust);
			MessagesUtil.createInfoNotification("Ingreso exitoso", "Se ha ingresado una justificación con normalidad.");
			newJust = JustificacionDTO.builder().build();
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
					.add("PF('dialogRegistrarJustificacion').hide()");
			rechargeListaJustificaciones();
		} catch (ServiciosException e) {
			MessagesUtil.createInfoNotification("Error de ingreso", e.getMessage());
		}
		
	}

	public void update() {
		try {
			Justificacion j = justificacionService.update(selectJust);
			MessagesUtil.createInfoNotification("Modificación exitosa",
					"Su justificación ha sido modificada con normalidad.");
			for (Justificacion justificacion : listaJustificaciones) {
				if (justificacion.getIdJustificacion() == j.getIdJustificacion()) {
					selectJust.setDetalle(j.getDetalle());
					selectJust.setFechaHora(j.getFechaHora());
					break;
				}
			}
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
			.add("PF('dialogModificarJustificacion').hide()");
			rechargeListaJustificaciones();
			
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error de modifiación", e.getMessage());
		}

	}

	public void delete(long idJust) {
		try {
			justificacionService.delete(idJust);
			MessagesUtil.createInfoMessage("Eliminado exitoso",
					"Se ha eliminado una justificación con normalidad.");
			rechargeListaJustificaciones();
		} catch (ServiciosException e) {
			MessagesUtil.createInfoNotification("Error al eliminar", e.getMessage());
		}

	}

	
	public void accionCreate () {
		
		try {
			justificacionService.actionCreate(accionJust, this.selectJust, idUsuario);
			MessagesUtil.createInfoNotification("Acción registrada", "Se ha registrado correctamente la acción sobre la justificacion");
			FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
			.add("PF('dialogAccionJustificacion').hide()");
		} catch (FieldValidationException e) {
			MessagesUtil.createErrorNotification("Error de validación", e.getMessage());
		} catch (ServiciosException e) {
			MessagesUtil.createErrorNotification("Error al crear acción", e.getMessage());
		}
	}
	
	
	public boolean globarlFilterFunctionJustificaciones(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Justificacion justificacion = (Justificacion) value;
		return (justificacion.getIdJustificacion() + "").toLowerCase().contains(filterText)
				|| justificacion.getEvento().getTitulo().toLowerCase().contains(filterText)
				|| (justificacion.getEstudiante().getDocumento() + "").toLowerCase().contains(filterText);
	}

	public void rechargeListaJustificaciones() {
		listaJustificaciones = justificacionService.getList(tipoUsuario, idUsuario);
		if (idEstadoFilter != null) {
			List<Justificacion> newList = new ArrayList<Justificacion>();
			for (Justificacion justificacion : listaJustificaciones){
				if (idEstadoFilter.longValue() == justificacion.getEstadoJustificacion().getId()) {
					newList.add(justificacion);
				}
			}
			listaJustificaciones.clear();
			listaJustificaciones.addAll(newList);
		}
		Ajax.update("form");
	}

	public void seleccionarConvocatoria(Convocatoria convocatoria) {
		Convocatoria conv = null;

		for (Convocatoria c : convocatoriaService.getAll()) {
			if (convocatoria.getIdConvocatoria() == c.getIdConvocatoria()) {
				conv = c;
			}
		}

		selectConvocatoria = conv;

		Ajax.update("form:dialogRegistrarJustificacionContent");
	}

	public void limpiarCampos() {

	}
}
