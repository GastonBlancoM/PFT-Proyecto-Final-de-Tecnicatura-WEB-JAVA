package com.exceptions;

public class PersistenciaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersistenciaException(String s,Throwable t) {
		super(s,t);
	}

	public PersistenciaException(String s) {
		super(s);
	}
}
