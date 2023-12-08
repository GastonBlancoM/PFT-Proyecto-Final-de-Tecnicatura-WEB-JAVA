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

import com.dao.SemestreDAO;
import com.entities.Evento;
import com.entities.Semestre;
import com.repositories.Repository;
import com.repositories.RepositoryEvento;
import com.repositories.RepositorySemestre;
import com.utils.JwtUtil;

@Path("/semestres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SemestreRest {
	
	@Inject
	@EJB
	(beanInterface = RepositorySemestre.class, beanName="SemestreDAO")
	private Repository<Semestre> semestreDAO;
	
	@GET
    @Path("/")
    public Response obtenerTodosLosSemestres(@HeaderParam("Authorization") String authorizationHeader) {
    	if (authorizationHeader == null) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	String jwtExtracted = authorizationHeader.substring("Bearer ".length()).trim();
    	if (jwtExtracted == null || !JwtUtil.isValidJwt(jwtExtracted)) {
    		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	return Response.ok(semestreDAO.getAll()).build();
    }
	
	
	  @GET
	  
	  @Path("/{id}") public Response obtenerSemestresPorId(@PathParam("id") Long
	  id, @HeaderParam("Authorization") String authorizationHeader) { if
	  (authorizationHeader == null) { return
	  Response.status(Response.Status.UNAUTHORIZED).build(); }
	  
	  String jwtExtracted =
	  authorizationHeader.substring("Bearer ".length()).trim(); if (jwtExtracted ==
	  null || !JwtUtil.isValidJwt(jwtExtracted)) { return
	  Response.status(Response.Status.UNAUTHORIZED).build(); }
	  
	  Semestre s = semestreDAO.get(id);
	  
	  if (s == null) { return Response.status(Response.Status.NOT_FOUND).build(); }
	  
	  return Response.ok(s).build(); }
	 
	
	/*
	 * @GET
	 * 
	 * @Path("/{id}") public Response obtenerSemestresPorId(@PathParam("id") Long
	 * id) { Semestre s = semestreDAO.get(id);
	 * 
	 * return Response.ok(s).build(); }
	 */
	
	
}
