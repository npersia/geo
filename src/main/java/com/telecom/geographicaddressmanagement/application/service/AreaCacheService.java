/**
 * 
 */
package com.telecom.geographicaddressmanagement.application.service;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public interface AreaCacheService {
	public void load();
	public void removeAll();
	public void reLoad();
}
