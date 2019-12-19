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
@ApiModel( description="Type")
public class Type {
	@ApiModelProperty(value = "Type name.", required = true, example = "Pais")
	private String name;
}
