/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.search.annotations.*;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@EqualsAndHashCode
@ApiModel( description="Padre de un area dentro de una jerarquia")
public class AreaFather {
	@ApiModelProperty(value = "Areas type", required = true, example = "Pais")
	@EqualsAndHashCode.Include
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String type;
	
	@ApiModelProperty(value = "Area name.", required = true, example = "ARGENTINA")
	@EqualsAndHashCode.Include
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String name;
	
	@ApiModelProperty(value = "nivel dentro de la jerarquia", required = true, example = "3")
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private Integer level;

}
