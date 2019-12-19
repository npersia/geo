/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Clase padre para las excepciones tecnicas
 * 
 * @author Pablo
 *
 */
public abstract class TechnicalException extends NestedRuntimeException { // NOSONAR

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            mensaje de la excepcion
	 */
	public TechnicalException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 *            mensaje de la excepcion
	 * @param cause
	 *            excecpion que le da origen
	 */
	public TechnicalException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
