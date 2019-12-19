/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.nopublic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.geographicaddressmanagement.application.service.AreaCacheService;
import com.telecom.geographicaddressmanagement.infraestucture.exception.RestException;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@RestController
@RequestMapping(value = "cache", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CacheResource {	
	private static final Logger logger = LoggerFactory.getLogger(CacheResource.class);
	
	@Autowired
	private AreaCacheService areaCacheService;
	
	@PostMapping(value="/area/clear")
	public ResponseEntity<Void> areaClear() {
		ResponseEntity<Void> result;

		try {		
			this.areaCacheService.removeAll();
			result = ResponseEntity.ok().build();			
			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al limpiar la cache de areas", ex);
			throw new RestException();
		}
	}
	
	@PostMapping(value="/area/load")
	public ResponseEntity<Void> areaLoad() {
		ResponseEntity<Void> result;

		try {
			this.areaCacheService.load();
			result = ResponseEntity.ok().build();			
			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al cargar la cache de areas", ex);
			throw new RestException();
		}
	}
	
	
	@PostMapping(value="/area/reload")
	public ResponseEntity<Void> areaReload() {
		ResponseEntity<Void> result;

		try {			
			this.areaCacheService.reLoad();
			result = ResponseEntity.ok().build();			
			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al recargar la cache de areas", ex);
			throw new RestException();
		}
	}

}
