/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.util.StringUtils;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;
import com.telecom.geographicaddressmanagement.resource.v1.response.AreaFather;
import com.telecom.geographicaddressmanagement.resource.v1.response.Characteristic;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT, nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface AreaMapper {
	
	public List<Area> dataAreasToAreas(List<DataArea> dataArea);
	
	public Area toArea(DataArea dataAreaSuggest);
	
	default List<AreaFather> toFathers(String chrFathers) {
		List<AreaFather> result = null;
		AreaFather areaFather = null;
		String[] fathers;
		String[] fatherProperties;
		
		
//		Sets.newHashSet(chrFathers.split("\\s*;\\s*")).stream().map(mapper)
		/*
		Arrays.asList(chrFathers.replaceFirst("/", "").split("/")).stream().map(
				father -> {
					AreaFather areaFatherr = null;
					String[] fatherPropertiess = null;
					
					fatherPropertiess = father.split("\\s*#\\s*");
					areaFatherr = new AreaFather();
					areaFatherr.setLevel(Integer.valueOf(fatherPropertiess[0]));
					areaFatherr.setType(fatherPropertiess[1]);
					areaFatherr.setName(fatherPropertiess[2]);
					return areaFatherr;
				}
		);
		*/
		if (StringUtils.hasText(chrFathers)) {
			result = new ArrayList<>();
			fathers = chrFathers.replaceFirst(";", "").split(";");
			for(String father : fathers) {
				fatherProperties = father.split("\\s*#\\s*");
				areaFather = new AreaFather();
				areaFather.setLevel(Integer.valueOf(fatherProperties[0]));
				areaFather.setType(fatherProperties[1]);
				areaFather.setName(fatherProperties[2]);
				result.add(areaFather);
			}
		} else {
			result = Collections.emptyList();
		}
		
		return result;
	}
	
	default List<Characteristic>  toCharacteristic(String cCharacteristics) {
		List<Characteristic> result = null;
		Characteristic areaCharacteristic = null;
		String[] characteristics = null;
		String[] characteristicProperties = null;
		
		if (StringUtils.hasText(cCharacteristics)) {
			result = new ArrayList<>();
			characteristics = cCharacteristics.split(";");
			for(String characteristic : characteristics) {
				characteristicProperties = characteristic.split("\\s*#\\s*");
				areaCharacteristic = new Characteristic();
				areaCharacteristic.setName(characteristicProperties[0]);
				areaCharacteristic.setValue(characteristicProperties[1]);
				result.add(areaCharacteristic);
			}
		} else {
			result = Collections.emptyList();
		}
		
		return result;
		
	}
//Coordenada X de punto medio#-62.0974521871761;Coordenada Y de punto medio#-32.6916031061651

}
