/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestGeographicAddressSearchByAddress {
	@ApiParam(value = "Country that the address is in.", required = true, example = "ARGENTINA")
	@NotEmpty
	@Size(max=50)
	private String country;
	@ApiParam(value = "The State or Province that the address is in.", required = true, example = "CORDOBA")
	@NotEmpty
	@Size(max=50)
	private String stateOrProvince;
	@ApiParam(value = "City that the address is in.", required = true, example = "22 PUNILLA")
	@NotEmpty
	@Size(max=50)
	private String city;
	@ApiParam(value = "Locality that the address is in.", required = true, example = "COSQUIN")
	@NotEmpty
	@Size(max=50)
	private String locality;
	@ApiParam(value = "Name of the street.", required = true, example = "IRUPE")
	@NotEmpty
	@Size(max=50)
	private String streetName;
	@ApiParam(value = "Number identifying a specific property on a public street.", required = true, example = "1452")
	@NotNull
	@Min(value=0)
	@Max(value=999999)	
	private int streetNr;

}
