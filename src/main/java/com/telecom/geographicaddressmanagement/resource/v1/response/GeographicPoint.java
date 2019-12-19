package com.telecom.geographicaddressmanagement.resource.v1.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel( description="Geographic Point.")
public class GeographicPoint {
	@ApiModelProperty(value = "x coordinate (usually latitude)", required = true, example = "3335.44335")
	private String x;
	@ApiModelProperty(value = "y coordinate (usually longitude)", required = true, example = "494987.09494")
	private String y;
	@ApiModelProperty(value = "z coordinate (usually elevation)", required = true, example = "0")
	private String z;
}
