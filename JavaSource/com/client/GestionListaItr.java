package com.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dto.ItrDTO;
import com.entities.Departamento;
import com.entities.Itr;
import com.entities.Localidad;
import com.enums.EstadoEvento;
import com.enums.EstadoItr;
import com.enums.Modalidad;
import com.enums.TipoEvento;
import com.exceptions.ServiciosException;
import com.services.DepartamentoService;
import com.services.ItrService;
import com.services.LocalidadService;
import com.utils.MessagesUtil;

import lombok.Data;

@Data
@SessionScoped
@Named(value = "gestionListaItr")
public class GestionListaItr implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GestionListaItr.class);
	
	private com.utils.ValidationError validationError = new com.utils.ValidationError();

	@Inject
	private ItrService itrService;
	@Inject
	private DepartamentoService departamentoService;
	@Inject
	private LocalidadService localidadService;

	private ItrDTO itrSeleccionado;
	private ItrDTO itrNuevo;
	
	private long idDeptoSelect;
	private long idLocalidadSelect;
	
	private List<Itr> listaItrs;
	private List<Departamento> listaDepartamentos;
	private List<Localidad> listaLocalidades;

	private EstadoItr estadoFilter;

	@PostConstruct
	private void init() {
		listaItrs = itrService.getAll();
		listaDepartamentos = departamentoService.obtenerTodosDepartamento();
		itrSeleccionado = ItrDTO.builder().departamento(new Departamento()).localidad(new Localidad()).build();
		itrNuevo = ItrDTO.builder().departamento(new Departamento()).localidad(new Localidad()).build();
	}

	// Esta función se ejecuta cuando se modifica un Itr
	public void seleccionarItr(Itr itr) {
		itrSeleccionado = itrService.itrToDTO(itr);
		cargarListaLocalidades(itrSeleccionado.getDepartamento().getIdDepartamento());
		Ajax.update("form:dialogModificarItrContent");
	}

	// Se ejecuta cuando se presiona el botón Guardar en ItrDialogRegistrar
	public void registrarItr() {
		itrNuevo.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDeptoSelect));
		itrNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSelect));
		
		try {
			if (validationError.validarErroresItr(itrNuevo)) {
				
				Itr itr = itrService.save(itrNuevo);		
				listaItrs.add(itr);
				itrNuevo = ItrDTO.builder().departamento(new Departamento()).localidad(new Localidad()).build();

				Ajax.update("form");
				Ajax.update("dialogs:dialogRegistrarItrContent");
				listaItrs = itrService.getAll();

				FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarItr').hide()");

				MessagesUtil.createInfoMessage("Registro exitoso", "Se ha registrado de forma satisfactoria el ITR");
			} else {
				MessagesUtil.createInfoMessage("Datos inválidos",
						"Algunos datos ingresados para el ITR no son válidos.");
			}
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error al registrar el ITR", e.getMessage());
		}
	}

	public void cargarListaLocalidades(long idDepartamento) {
		Departamento departamento = departamentoService.obtenerPorIdDepartamento(idDepartamento);
		listaLocalidades = localidadService.obtenerLocalidadesPorDepartamento(departamento);
	}

	// Se ejecuta cuando se presiona el botón Guardar en ItrDialogModificar
	public void modificarItr() {
		itrSeleccionado.setDepartamento(departamentoService.obtenerPorIdDepartamento(idDeptoSelect));
		itrSeleccionado.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidadSelect));
		
		long idLocalidad = itrSeleccionado.getLocalidad().getIdLocalidad();
		itrSeleccionado.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidad));

		try {
			if (validationError.validarErroresItr(itrSeleccionado)) {
				Itr actualizardItr = itrService.update(itrSeleccionado);
				for (Itr itr : listaItrs) {
					if (itr.getIdItr() == actualizardItr.getIdItr()) {
						itr.setNombre(actualizardItr.getNombre());
						itr.setIdItr(actualizardItr.getIdItr());
						itr.setLocalidad(actualizardItr.getLocalidad());
						itr.setEstadoItr(actualizardItr.getEstadoItr());
						break;
					}
				}
				
				MessagesUtil.createInfoMessage("Modificación exitosa", "Se ha modificado de forma satisfactoria el ITR");				
				Ajax.update("form");
			}else {
				MessagesUtil.createInfoMessage("Datos inválidos", "Algunos datos ingresados para el ITR no son válidos.");
				Ajax.update("form");
			}
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error al modificar el ITR", e.getMessage());
			Ajax.update("form");
		}
	}

	// Se ejecuta cuando se presiona el botón Eliminar en ItrDataTable
	public void eliminarItrLogicamente(long id) {
		try {
			itrService.borrarItrLogicamente(id);

			for (Itr itr : listaItrs) {
				if (itr.getIdItr() == id) {
					itr.setEstadoItr(EstadoItr.DESACTIVADO);
					break;
				}
			}
			
			MessagesUtil.createInfoMessage("Baja exitosa", "Se ha eliminado de forma satisfactoria el ITR");
			Ajax.update("form");
		} catch (ServiciosException e) {
			MessagesUtil.createErrorMessage("Error al eliminar el ITR", e.getMessage());
			Ajax.update("form");
		}
	}

	public void recargarListaItr() {
		listaItrs = itrService.getAll();
		Ajax.update("form");
	}
	
	public void refreshListaItr(EstadoItr estado) {
		listaItrs = new LinkedList<>();
		if (estado != null) {
			for (Itr itr : itrService.getAll()) {
				if (itr.getEstadoItr().equals(estado)) {
					listaItrs.add(itr);
				}
			}
		}else {
			listaItrs = itrService.getAll();
		}
		Ajax.update("form");
	}
	
	public void limpiarCampos() {
		itrSeleccionado = ItrDTO.builder().build();
		itrNuevo = ItrDTO.builder().build();
	}

	public String getTagSeverity(EstadoItr estado) {
		return EstadoItr.ACTIVADO.equals(estado) ? "success" : "danger";
	}

	public boolean globalFilterFunctionItr(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		boolean filterTextCondition = !LangUtils.isBlank(filterText);

		Itr itr = (Itr) value;
		boolean estadoCondition = estadoFilter == null || itr.getEstadoItr().equals(estadoFilter);

		if (filterTextCondition) {
			boolean idCondition = (itr.getIdItr() + "").toLowerCase().contains(filterText);
			boolean itrCondition = itr.getNombre().toLowerCase().contains(filterText);
			boolean localidadCondition = itr.getLocalidad().getNombre().toLowerCase().contains(filterText);
			boolean departamentoCondition = itr.getLocalidad().getDepartamento().getNombre().toLowerCase()
					.contains(filterText);
			return estadoCondition && (idCondition || itrCondition || localidadCondition || departamentoCondition);
		} else {
			return estadoCondition;
		}
	}
	
	

}
