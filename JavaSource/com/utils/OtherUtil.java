package com.utils;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class OtherUtil {
	
	public static String anioParaCalendario() {
		LocalDate fechaActual = LocalDate.now();
		int a = fechaActual.getYear() - 100;
		int b = fechaActual.getYear() + 10;
		return a + ":" + b;
	}
	
	public static <T> T findByIdOrNull(Class<T> clase, long id, EntityManager em) {
		try {
			return em.find(clase, id);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public static <T> T findByQueryOrNull(Class<T> clase, TypedQuery<T> query, EntityManager em) {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
