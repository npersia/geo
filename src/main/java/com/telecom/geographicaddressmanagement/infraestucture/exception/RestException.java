/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.exception;

/**
 * Representa las excepciones que se van a publicar fuera del servicio rest
 * 
 * @author Pablo
 *
 */
public class RestException extends TechnicalException {// NOSONAR

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public RestException() {
		super("Error Tecnico. Conactate al administrador");
	}

}
