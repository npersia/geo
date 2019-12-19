/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.exception;

import org.springframework.core.NestedCheckedException;

/**
 * Clase padre para la excepciones de negocio
 * 
 * @author Pablo
 *
 */
public abstract class BusinessException extends NestedCheckedException { // NOSONAR

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            mensaje de la excepcion
	 */
	public BusinessException(String msg) {
		super(msg);
	}

}
