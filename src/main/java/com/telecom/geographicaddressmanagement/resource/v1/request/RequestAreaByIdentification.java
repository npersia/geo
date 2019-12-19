/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
public class RequestAreaByIdentification {
	@ApiParam(value = "unique identification", required = true, allowMultiple = true, example = "CAPITAL FEDERAL")
	@NotEmpty
	@Size(max=20)
	private List<@NotBlank String> identification;
	
	@ApiParam(value = "area type", required = true, example = "Localidades")
	@NotBlank
	@Size(min=1, max=50)
	private String type;
}
