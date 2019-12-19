/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecom.geographicaddressmanagement.config.SwaggerConfig;

import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.search.annotations.*;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
@ApiModel( description=SwaggerConfig.TAG_AREA_DESCRIPTION)
@Indexed
public class Area {
	
	@JsonIgnore
	@Autowired
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private Long id;
	
	@ApiModelProperty(value = "Area name.", required = true, example = "ARGENTINA")
	@Fields({
		@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword")),
        @Field(name = "sortName", analyze = Analyze.NO, store = Store.NO, index = Index.NO)
    })
	@SortableField(forField = "sortName")
	private String name;
	
	@ApiModelProperty(value = "Identification", required = true, example = "ARGENTINA,BUENOS AIRES,TRES FEBRERO,CHURRUCA")
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String identification;
	
	@ApiModelProperty(value = "Areas type", required = true, example = "Paises")
	@Field(store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "keyword"))
	private String type;
	
	@ApiModelProperty(value = "List of characteristics")
	private List<Characteristic> characteristics = new ArrayList<>();
	
	@ApiModelProperty(value = "List of area father")
	@IndexedEmbedded
	@FieldBridge(impl = AreaFather.class)
	private List<AreaFather> fathers = new ArrayList<>(); 
}