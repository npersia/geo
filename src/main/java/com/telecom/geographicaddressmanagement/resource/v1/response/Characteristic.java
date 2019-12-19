/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import org.hibernate.search.annotations.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description="Name/value pairs, used to extra characterized the Area")
public class Characteristic {
	@ApiModelProperty(value = "Name of the characteristic.", required = true, example = "Codigo Postal")
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String name;

	@ApiModelProperty(value = "Value of the characteristic.", required = true, example = "1234")
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String value;
}
