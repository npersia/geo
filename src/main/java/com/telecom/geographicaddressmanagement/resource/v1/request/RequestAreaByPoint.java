/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.request;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
public class RequestAreaByPoint {
	@ApiParam(value = "x coordinate (WGS84 coordinate system))", required = true, example = "-64.4780764761547") 
	@NotNull
	private Double x; 
	
	@ApiParam(value = "y coordinate (WGS84 coordinate system)", required = true, example = "-31.2470345044283")  
	@NotNull
	private Double y;
	
	@ApiParam(value = "area type", required = true, example = "Hubs", allowMultiple=true) 
    @Size(min=1, max=10) 
	private List<@Size(min=1, max=50) @NotBlank String> type;
	
	@ApiParam(value = "Requested number of resources to be provided in response requested by client. Default value 999", defaultValue = "999", example = "999") 
	@Min(0)
	@Max(999)
	private Integer limit = 999;
	
	@ApiParam(value = "Requested index for start of resources to be provided in response requested by clien. Default value 0",  defaultValue = "0", example = "0") 
	@Min(0)
	@Max(999)
	private Integer offset = 0;
}
