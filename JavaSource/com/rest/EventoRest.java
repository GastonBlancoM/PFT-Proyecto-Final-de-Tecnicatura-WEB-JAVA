package com.rest;


import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.entities.Evento;
import com.repositories.RepositoryEvento;
import com.utils.JwtUtil;



@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoRest {
	
	@Inject
	@EJB private RepositoryEvento eventoDAO;
	
	@GET
    @Path("/")
    public Response obtenerTodosLosEventos(@HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	return Response.ok(eventoDAO.getAll()).build();
    }
	
	@GET
    @Path("/{id}")
    public Response obtenerEventoPorId(@PathParam("id") Long id, @HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	Evento eve = eventoDAO.get(id);
    	
    	if (eve == null) {
    		return Response.status(Response.Status.NOT_FOUND).build();
		}
    	
        return Response.ok(eve).build();
    }
	
//	@GET
//    @Path("/{id}")
//	public Response obtenerEventoPorId(@PathParam("id") Long id) {
//		Evento eve = eventoDAO.get(id);
//		
//		return Response.ok(eve).build();
//	}
}
