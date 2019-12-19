/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import java.util.List;

import com.telecom.geographicaddressmanagement.config.SwaggerConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description=SwaggerConfig.TAG_GEOGRAPHIC_ADDRESS_DESCRIPTION)
public class GeographicAddress {	
	@ApiModelProperty(value = "Country that the address is in.", required = true, example = "ARGENTINA")
	private String country;
	@ApiModelProperty(value = "The State or Province that the address is in.", required = true, example = "CAPITAL FEDERAL")
	private String stateOrProvince;
	@ApiModelProperty(value = "City that the address is in.", required = true, example = "CAPITAL FEDERAL")
	private String city;
	@ApiModelProperty(value = "Locality that the address is in.", required = true, example = "CAPITAL FEDERAL")
	private String locality;
	@ApiModelProperty(value = "Name of the street.", required = true, example = "CORRIENTES")
	private String streetName;
	@ApiModelProperty(value = "Number identifying a specific property on a public street.", required = true, example = "669")
	private Integer streetNr;
	@ApiModelProperty(value = "Street type: Alley, avenue, boulevard, brae, crescent, drive, highway, lane, terrace, parade, place, tarn, way, wharf", required = true, example = "AVENIDA")
	private String streetType;
	@ApiModelProperty(value = "Descriptor for a postal delivery area, used to speed and simplify the delivery of mail (also known as zipcode)", required = true, example = "1414")
	private String postcode;
	@ApiModelProperty(value = "Even, or odd  or both, number side street", required = true, example = "I")
	private String streetNumberSide;
	@ApiModelProperty(value = "Beetween streets of address.", required = true)
	private BeetweenStreet beetweenStreet;
	@ApiModelProperty(value = "Geographic data location.", required = true)
	private GeographicLocation geographicLocation;
	@ApiModelProperty(value = "List zones on address", required = true)
	private List<RelationsArea> zones;
	@ApiModelProperty(value = "Points of interest at address", required = true)
	private List<RelationsPointOfInterest> pointsOfInterest;
}
