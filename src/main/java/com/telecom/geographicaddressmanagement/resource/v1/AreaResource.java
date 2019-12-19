/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.telecom.geographicaddressmanagement.config.SwaggerConfig;
import com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository;
import com.telecom.geographicaddressmanagement.domain.repository.AreaRepository;
import com.telecom.geographicaddressmanagement.infraestucture.exception.RestException;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestAreaByIdentification;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestAreaByName;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestAreaByPoint;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestAreaFullTextSearch;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;

import com.telecom.geographicaddressmanagement.application.service.impl.AreaCacheServiceImpl;

import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.Query;	


/**
 * @author PABLODANIELDELBUONO
 *
 */
@RestController
@RequestMapping(value = "v1/areas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // , consumes =
																						// MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { SwaggerConfig.TAG_AREA })
public class AreaResource {
	private static final Logger logger = LoggerFactory.getLogger(AreaResource.class);
	
	@Value("${resource.cache.area:60}")
	private int HTTP_CACHE_MAX_AGE; 

	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private AreaFullTextSearchRepository fulltextSearchRepository;
	
	@GetMapping(params= {"x","y","type"})
	@ApiOperation(value = "Get area of type parameter on (x,y)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved data from area."),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found areas.") })
	public ResponseEntity<List<Area>> searchByGeoAndType(@Valid RequestAreaByPoint requestAreaByPoint) {
		List<Area> areasPage = null;
		ResponseEntity<List<Area>> result;
		List<Long> areaIdentifiers = null;
		int cant_total_result = 0;

		try {
			areaIdentifiers = areaRepository.getBy(requestAreaByPoint.getX(), requestAreaByPoint.getY(), requestAreaByPoint.getType());
			logger.debug("By x,y: {}", areaIdentifiers);
			
			if (! CollectionUtils.isEmpty(areaIdentifiers)) {
				areaIdentifiers = requestAreaByPoint.getType().stream().map(String::toUpperCase).map(fulltextSearchRepository::getByType).flatMap(Set::stream).filter(areaIdentifiers::contains).collect(Collectors.toList());
				logger.debug("type: {}", areaIdentifiers);
				
				
				cant_total_result = areaIdentifiers.size();					
				areasPage = areaIdentifiers.stream().map(fulltextSearchRepository::get).sorted(Comparator.comparing(Area::getName)).skip(requestAreaByPoint.getOffset()*requestAreaByPoint.getLimit()).limit(requestAreaByPoint.getLimit()).collect(Collectors.toList());
				logger.debug("Areas: {}", areasPage);
			}
			
			if (CollectionUtils.isEmpty(areasPage)) {
				result = ResponseEntity.notFound().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).build();
			} else {
				if (cant_total_result == areasPage.size()) {
					result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).header("X-TOTAL-COUNT", String.valueOf(cant_total_result)).body(areasPage);
				} else {
					result = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).cacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS)).header("X-TOTAL-COUNT", String.valueOf(cant_total_result)).body(areasPage);
				}
			}		

			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al obtener el area por x,y", ex);
			throw new RestException();
		}
	}
	
	@GetMapping(params= {"name", "type"})
	@ApiOperation(value = "Get area by name and type")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved data from area."),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found areas.") })
	public ResponseEntity<List<Area>> searchByName(@Valid RequestAreaByName requestAreaByName) {
		Long idArea = null;
		Area areaOldResult = null;
		List<Area> areas = null;
		ResponseEntity<List<Area>> result = null;

		try {
			areas = areaRepository.getAreaByNameAndType(requestAreaByName.getName().toUpperCase(), requestAreaByName.getType());

			if (areas == null) {
				result = ResponseEntity.notFound().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).build();				
			} else {								
				result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).body(areas);
			}
			
			return result;			
		} catch (Exception ex) {
			logger.error("Error de ejecucion al obtener el area por nombre", ex);
			throw new RestException();
		}
	}
	
	@GetMapping(params= {"identification", "type"})
	@ApiOperation(value = "Get area by identification")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved data from area."),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found areas.") })
	public ResponseEntity<Area> searchByIdentification(@Valid RequestAreaByIdentification requestAreaByIdentification) {
		Area area = null;	
		ResponseEntity<Area> result;

		try {
			area = this.areaRepository.getAreaByTypeAndIdentification(requestAreaByIdentification.getType(), String.join(",", requestAreaByIdentification.getIdentification()));						

			if (area == null) {
				result = ResponseEntity.notFound()
						.cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).build();
			} else {
				result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).header("X-TOTAL-COUNT", "1")
						.body(area);
			}
			
			return result;			
		} catch (Exception ex) {
			logger.error("Error de ejecucion al obtener el area por nombre", ex);
			throw new RestException();
		}
	}
	
	@GetMapping(params= {"name", "type", "fullText=true"})
	@ApiOperation(value = "Full text search by area name")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved data from area."),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found areas.") })
	public ResponseEntity<List<Area>> searchByName(@Valid RequestAreaFullTextSearch requestAreaByName) {
		ResponseEntity<List<Area>> result = null;
		List<Area> areasPage = null;
		Stream<Area> areasPageFilteredByTypes = null;

		Set<Long> areaIdentifiers = null;
		int cant_total_result = 0;
		List<String> typesListNames = requestAreaByName.getType();

		areasPage = areaRepository.getAreaAsFullTextSearch(
			requestAreaByName.getName(),
			typesListNames,
			requestAreaByName.getFullText(),
			requestAreaByName.getFatherType(),
			requestAreaByName.getFatherName(),
			requestAreaByName.getOffset(),
			requestAreaByName.getLimit()
		);

		if (requestAreaByName.getType().size() > 0 ){
			areasPageFilteredByTypes = areasPage.stream().filter( area -> requestAreaByName.getType().contains( area.getType() )  );
			areasPage = areasPageFilteredByTypes.collect(Collectors.toList());
		}			

		try {			
			if (CollectionUtils.isEmpty(areasPage)) {
				result = ResponseEntity.notFound().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).build();
			} else {
				result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).body(areasPage);
			}		

			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al obtener el area por suggest", ex);
			throw new RestException();
		}
	}
}
