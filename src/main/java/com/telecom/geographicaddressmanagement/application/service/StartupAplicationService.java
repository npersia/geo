/**
 * 
 */
package com.telecom.geographicaddressmanagement.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Component
public class StartupAplicationService {

//	private static final Logger logger = LoggerFactory.getLogger(StartupAplicationService.class);

	@Autowired
	private AreaCacheService cacheService;
	
	@EventListener
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		this.cacheService.load();
	}		
}
