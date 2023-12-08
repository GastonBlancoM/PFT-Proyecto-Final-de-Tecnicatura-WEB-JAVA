package com.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dto.ItrDTO;
import com.entities.Itr;
import com.entities.Localidad;
import com.exceptions.ServiciosException;
import com.repositories.Repository;

import lombok.Data;


@Data
@Stateless
@LocalBean
public class ItrService implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(beanInterface = Repository.class, beanName="ItrDAO")
	private Repository<Itr> itrDAO;
	
	@Inject private LocalidadService localidadService;
	
	public Itr save(ItrDTO DTO) throws ServiciosException {
		Itr itrNuevo = dtoToItr(DTO);
		long idLocalidad = itrNuevo.getLocalidad().getIdLocalidad();
		itrNuevo.setLocalidad(localidadService.buscarLocalidadPorId(idLocalidad));
		itrDAO.save(itrNuevo);
		return itrNuevo;
	}
	
	public Itr update(ItrDTO DTO) throws ServiciosException {
		Itr itr = dtoToItr(DTO);
		itrDAO.update(itr);
		return itr;
	}
	
	public void borrarItrLogicamente(long id) throws ServiciosException {
		itrDAO.delete(itrDAO.get(id));
	}
	
	public List<Itr> getAll(){
		return itrDAO.getAll();
	}
	
	public Itr get (long id) {
		return itrDAO.get(id);
	}
	
	public Itr dtoToItr (ItrDTO DTO) {
		Itr itr = new Itr();
		itr.setIdItr(DTO.getIdItr());
		itr.setNombre(DTO.getNombre());
		itr.setLocalidad(DTO.getLocalidad());
		itr.setEstadoItr(DTO.getEstado());
		return itr;
	}
	
	public ItrDTO itrToDTO(Itr itr) {
		return itr == null
				? null
				: ItrDTO.builder()
					.idItr(itr.getIdItr())
					.nombre(itr.getNombre())
					.localidad(itr.getLocalidad())
					.estado(itr.getEstadoItr())
					.departamento(
						localidadExiste(itr.getLocalidad())
							? itr.getLocalidad().getDepartamento()
							: null)
					.build();
	}
	
	private boolean localidadExiste(Localidad l) {
		return l != null;
	}
	
}
