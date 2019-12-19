/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telecom.geographicaddressmanagement.config.SwaggerConfig;
import com.telecom.geographicaddressmanagement.domain.repository.AreaRepository;
import com.telecom.geographicaddressmanagement.infraestucture.exception.RestException;
import com.telecom.geographicaddressmanagement.resource.v1.response.Type;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@RestController
@RequestMapping(value = "v1/types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = { SwaggerConfig.TAG_TYPES })
public class TypeResource {
	private static final Logger logger = LoggerFactory.getLogger(TypeResource.class);
	
	@Value("${resource.cache.type:60}")
	private int HTTP_CACHE_MAX_AGE;

	@Autowired
	private AreaRepository areaRepository;

	@GetMapping("areas")
	@ApiOperation(value = "Get areas types")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved areas types"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found areas types.") })

	public ResponseEntity<List<Type>> searchByGeoAndType() {
		List<Type> types = null;
		ResponseEntity<List<Type>> result;

		try {
			types = this.areaRepository.getTypes();

			if ((types == null) || (types.isEmpty())) {
				result = ResponseEntity.notFound().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).build();
			} else {
				result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).header("X-TOTAL-COUNT", String.valueOf(types.size())).body(types);
			}

			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecución al obtener el punto de interes", ex);
			throw new RestException();
		}
	}
}
