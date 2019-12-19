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
@ApiModel( description="Zones on address.")
public class RelationsArea {
	@ApiModelProperty(value = "Area type", required = true, example = "country")
	private String type;
	@ApiModelProperty(value = "Area name", required = true, example = "ARGENTINA")
	private String value;
}
