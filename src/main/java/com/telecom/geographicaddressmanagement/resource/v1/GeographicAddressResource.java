/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1;

import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

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
import com.telecom.geographicaddressmanagement.domain.model.DataAddress;
import com.telecom.geographicaddressmanagement.domain.repository.GeographicAddressRepository;
import com.telecom.geographicaddressmanagement.infraestucture.exception.RestException;
import com.telecom.geographicaddressmanagement.resource.v1.mapper.GeographicAddressMapper;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestGeographicAddressSearchByAddress;
import com.telecom.geographicaddressmanagement.resource.v1.response.GeographicAddress;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@RestController
@RequestMapping(value = "v1/geographicAddress", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // , consumes =
																									// MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { SwaggerConfig.TAG_GEOGRAPHIC_ADDRESS })
public class GeographicAddressResource {
	private static final Logger logger = LoggerFactory.getLogger(GeographicAddressResource.class);
	
	@Value("${resource.cache.address:60}")
	private int HTTP_CACHE_MAX_AGE;

	@Autowired
	private GeographicAddressRepository repository;

	@Autowired
	private GeographicAddressMapper mapper;

	// http://localhost:8080/greographicAddressManagment/v1/geographicAddress?country=ARGENTINA&stateOrProvince=CORDOBA&city=22%20PUNILLA&locality=COSQUIN&streetName=IRUPE&streetNr=1452
	// http://localhost:8080/v1/geographicAddress?country=ARGENTINA&stateOrProvince=CORDOBA&city=22%20PUNILLA&locality=COSQUIN&streetName=IRUPE&streetNr=1452

	@GetMapping
	@ApiOperation(value = "This operation is used to retrieve geographic addresses corresponding to search criteria. Filtering is allowed on all attributes", response = GeographicAddress.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved data from address."),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found. Not found address.") })
	public ResponseEntity<GeographicAddress> searchByAddress(
			final @Valid RequestGeographicAddressSearchByAddress requestSearchByAddress) {
		GeographicAddress geographicAddress = null;
		ResponseEntity<GeographicAddress> result = null;
		DataAddress dataAddress = null;

		try {
			dataAddress = this.repository.getDataAddressBy(requestSearchByAddress);

			if (dataAddress == null) {
				result = ResponseEntity.notFound().cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).build();
			} else {
				geographicAddress = mapper.dataAddressToGeographicAddress(requestSearchByAddress, dataAddress);
				result = ResponseEntity.ok().cacheControl(CacheControl.maxAge(HTTP_CACHE_MAX_AGE, TimeUnit.SECONDS)).body(geographicAddress);
			}

			return result;
		} catch (Exception ex) {
			logger.error("Error de ejecucion al obtener el datos dela direccion", ex);
			throw new RestException();
		}

	}
}
