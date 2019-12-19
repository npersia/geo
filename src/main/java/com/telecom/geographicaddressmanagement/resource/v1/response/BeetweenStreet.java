package com.telecom.geographicaddressmanagement.resource.v1.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description="Beetween streets of address.")
public class BeetweenStreet {
	@ApiModelProperty(value = "Intersection left street.", required = true, example = "SERRANO")
	private String intersectionLeft;
	@ApiModelProperty(value = "Intersection right street.", required = true, example = "THAMES")
	private String intersectionRight;
	

}
