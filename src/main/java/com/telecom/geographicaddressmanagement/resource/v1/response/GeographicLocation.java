/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description="Geographic data location.")
public class GeographicLocation {
	@ApiModelProperty(value = "Geocoding referential.", required = true, example = "WGS84")
	private String spatialRef;
	@ApiModelProperty(value = "Type allows describing Geolocation form: it could be a point, a line, a polygon, a cylinder, etc.", required = true, example = "point")
	private String geometryType;
	@ApiModelProperty(value = "A list of geographic points. A GeographicPoint defines a geographic point through coordinates.", required = true)
	private List<GeographicPoint> geometry;

}
