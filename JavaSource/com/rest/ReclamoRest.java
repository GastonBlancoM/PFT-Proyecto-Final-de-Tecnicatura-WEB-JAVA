package com.rest;

import java.util.Date;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dto.ReclamoDTO;
import com.entities.Evento;
import com.entities.Reclamo;
import com.entities.Semestre;
import com.enums.EstadoReclamo;
import com.enums.EstadoUsuario;
import com.exceptions.ServiciosException;
import com.repositories.RepositoryReclamo;
import com.services.EventoService;
import com.services.ReclamoService;
import com.services.SemestreService;
import com.utils.FieldValidationException;
import com.utils.JwtUtil;
import com.utils.ValidationError;

@Path("/reclamos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReclamoRest {

	@EJB private RepositoryReclamo dao;
	
	@Inject private EventoService eventoService;
	@Inject private SemestreService semestreService;
	@Inject private ReclamoService reclamoService;
	
	 @GET
	    @Path("/")
	    public Response obtenerTodosLosReclamos(@HeaderParam("Authorization") String authorizationHeader) {
	    	if (authorizationHeader == null) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
	    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	return Response.ok(dao.getAll()).build();
	    }
	 
	 @GET
	    @Path("/{id}")
	    public Response obtenerReclamoPorId(@PathParam("id") Long id, @HeaderParam("Authorization") String authorizationHeader) {
	    	if (authorizationHeader == null) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
	    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	Reclamo rec = dao.get(id);
	    	
	    	if (rec == null) {
	    		return Response.status(Response.Status.NOT_FOUND).build();
			}
	    	
	        return Response.ok(rec).build();
	    }
	 
	 @POST
	    public Response crearNuevoReclamo(ReclamoDTOrest reclamo, @HeaderParam("Authorization") String authorizationHeader) {
		 	System.out.println(reclamo + "rest crear nuevo reclamos");
		 
	    	if (authorizationHeader == null) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
	    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	        try {
	        	ReclamoDTO dtoNormal = new ReclamoDTO();
	        	dtoNormal.setTitulo(reclamo.getTitulo());
	        	dtoNormal.setDetalle(reclamo.getDetalle());
	        	dtoNormal.setTipoReclamo(reclamo.getTipoReclamo());
	        	
	        	Evento eve = eventoService.get(reclamo.getIdEvento());
	        	dtoNormal.setEvento(eve);
	        	
	        	Semestre sem = semestreService.buscarPorId(reclamo.getIdSemestre());
	        	dtoNormal.setSemestre(sem);
	        	
	        	dtoNormal.setEstadoReclamo(reclamo.getEstadoReclamo());
	        	
	        	ValidationError.validarReclamo(dtoNormal);
	        	dao.save(reclamoService.dtoRestToReclamo(reclamo));
	        	return Response.status(Response.Status.CREATED).entity(reclamo).build();
	        	
			} catch (ServiciosException e) {
				
				String errorMessage = e.getMessage();
			    return Response.status(Response.Status.BAD_REQUEST)
			            .entity(errorMessage).build();
			}
	        catch (FieldValidationException e) {
				
				
				String errorMessage = e.getMessage();
			    return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			    
			}
	        
	    }
	 
	 @PUT
	    @Path("/")
	    public Response actualizarReclamo(ReclamoDTOrest reclamo, @HeaderParam("Authorization") String authorizationHeader) {
	    	if (authorizationHeader == null) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
	    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	        
	        try {
	        	ReclamoDTO dtoNormal = new ReclamoDTO();
	        	dtoNormal.setTitulo(reclamo.getTitulo());
	        	dtoNormal.setDetalle(reclamo.getDetalle());
	        	dtoNormal.setTipoReclamo(reclamo.getTipoReclamo());
	        	dtoNormal.setFecha_Hora(new Date());
	  
	        	
	        	Reclamo re= reclamoService.get(reclamo.getIdReclamo());
	        	dtoNormal.setEstadoReclamo(re.getEstadoReclamo());
	        	dtoNormal.setEstudiante(re.getEstudiante());
	        	reclamo.setEstadoReclamo(re.getEstadoReclamo());
	        	
	        	
	        	Evento eve = eventoService.get(reclamo.getIdEvento());
	        	dtoNormal.setEvento(eve);
	        	
	        	Semestre sem = semestreService.buscarPorId(reclamo.getIdSemestre());
	        	dtoNormal.setSemestre(sem);
	        	
	        	
	        	
	        	
				ValidationError.validarReclamo(dtoNormal);
	        	dao.update(reclamoService.dtoRestToReclamo(reclamo));
				
				System.out.println("RECLAMO 1" + re);
				System.out.println("RECLAMO 2" + dtoNormal);
	        	return Response.status(Response.Status.CREATED).entity(reclamo).build();
	        	
			} catch (ServiciosException e) {
				
				String errorMessage = e.getMessage();
			    return Response.status(Response.Status.BAD_REQUEST)
			            .entity(errorMessage).build();
			}
			    
			 catch (FieldValidationException e) {
				
				
				String errorMessage = e.getMessage();
			    return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			    
			}
	    }
	 
	 @DELETE
	    @Path("/{id}")
	    public Response eliminarReclamo(@PathParam("id") Long id, @HeaderParam("Authorization") String authorizationHeader) {    	
	    	if (authorizationHeader == null) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
	    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	try {
	    		dao.delete(dao.get(id));
	    		JsonObject jsonResponse = Json.createObjectBuilder()
	                    .add("mensaje", "Baja lógica exitosa")
	                    .build();
	            return Response.status(Response.Status.OK)
	                    .entity(jsonResponse.toString())
	                    .type(MediaType.APPLICATION_JSON)
	                    .build();
			} catch (ServiciosException e) {
				e.printStackTrace();
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
	    }
	
}
