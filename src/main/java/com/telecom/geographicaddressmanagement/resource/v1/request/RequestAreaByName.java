/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
public class RequestAreaByName {
	@ApiParam(value = "area name", required = true, example = "CAPITAL FEDERAL") 
	@NotBlank 
    @Size(min=1, max=50) 
	private String name; 
	
	@ApiParam(value = "area type", required = true, example = "Localidades")
	@NotBlank
	@Size(min=1, max=50)
	private String type;
	
	@ApiParam(value = "Requested number of resources to be provided in response requested by client. Default value 999", defaultValue = "999", example = "999") 
	@Min(0)
	@Max(999)
	private Integer limit = 999;
	
	@ApiParam(value = "Requested index for start of resources to be provided in response requested by clien. Default value 0",  defaultValue = "0", example = "0") 
	@Min(0)
	@Max(999)
	private Integer offset = 0;

//	@ApiParam(value = "name fathers", required = true, allowMultiple = true, example = "CAPITAL FEDERAL")
//	@NotEmpty
//	@Size(max=20)
//	private List<@NotBlank String> name;

}
