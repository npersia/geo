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
public class RequestAreaFullTextSearch {
	@ApiParam(value = "area name", required = true, example = "CAP") 
	@NotBlank 
    @Size(min=3, max=50) 
	private String name; 
		
	@ApiParam(value = "area type", required = true, example = "Localidades", allowMultiple=true) 
    @Size(min=1, max=10) 
	private List<@Size(min=1, max=50) @NotBlank String> type;
	
	
	@ApiParam(value = "full text search", required = true, example = "true") 
	@NotNull
	private Boolean fullText;
		
	
	@ApiParam(value = "area father type", example = "Pais") 
	@Size(min=0, max=50)
	private String fatherType;
	
	@ApiParam(value = "area father name", example = "ARGENTINA") 
	@Size(min=0, max=50)
	private String fatherName;
	
	@ApiParam(value = "Requested number of resources to be provided in response requested by client. Default value 999", defaultValue = "999", example = "999") 
	@Min(0)
	@Max(999)
	private Integer limit = 999;
	
	@ApiParam(value = "Requested index for start of resources to be provided in response requested by clien. Default value 0",  defaultValue = "0", example = "0") 
	@Min(0)
	@Max(999)
	private Integer offset = 0;
	
}
