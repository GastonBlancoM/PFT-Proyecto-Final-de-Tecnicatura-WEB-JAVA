package com.utils;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

public class MessagesUtil {
	// FacesMessage
	// SEVERITY_INFO, SEVERITY_WARN, SEVERITY_ERROR, SEVERITY_FATAL
	
	public static void createInfoMessage(String title, String message) {
		createMessage(FacesMessage.SEVERITY_INFO, title, message);
	}
	
	public static void createWarnMessage(String title, String message) {
		createMessage(FacesMessage.SEVERITY_WARN, title, message);
	}
	
	public static void createErrorMessage(String title, String message) {
		createMessage(FacesMessage.SEVERITY_ERROR, title, message);
	}
	
	public static void createInfoNotification(String title, String message) {
		createNotification(FacesMessage.SEVERITY_INFO, title, message);
	}
	
	public static void createWarnNotification(String title, String message) {
		createNotification(FacesMessage.SEVERITY_WARN, title, message);
	}
	
	public static void createErrorNotification(String title, String message) {
		createNotification(FacesMessage.SEVERITY_ERROR, title, message);
	}
	
	private static void createMessage(Severity severity, String title, String message) {
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(severity, title, message));
	}
	
	private static void createNotification(Severity severity, String title, String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
	}
}
