/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description="Point of interest on address.")
public class RelationsPointOfInterest {
	@ApiModelProperty(value = "Point of interest type", required = true, example = "Edificio")
	private String type;
	@ApiModelProperty(value = "Indentifier", required = true, example = "1234")
	private String id;

}
