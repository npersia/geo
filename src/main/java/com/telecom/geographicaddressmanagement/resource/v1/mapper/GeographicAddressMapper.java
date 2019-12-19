/**
 * 
 */
package com.telecom.geographicaddressmanagement.resource.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import com.telecom.geographicaddressmanagement.domain.model.DataAddress;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestGeographicAddressSearchByAddress;
import com.telecom.geographicaddressmanagement.resource.v1.response.GeographicAddress;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Mapper(componentModel = "spring", nullValueMappingStrategy=NullValueMappingStrategy.RETURN_DEFAULT)
public interface GeographicAddressMapper {
	@Mapping(source="requestSearchByAddress.country", target = "country")
	@Mapping(source="requestSearchByAddress.stateOrProvince", target = "stateOrProvince")
	@Mapping(source="requestSearchByAddress.city", target = "city")
	@Mapping(source="requestSearchByAddress.locality", target = "locality")
	@Mapping(source="requestSearchByAddress.streetName", target = "streetName")
	@Mapping(source="requestSearchByAddress.streetNr", target = "streetNr")	
	@Mapping(source="dataAddress.streetType", target = "streetType")
	@Mapping(source="dataAddress.postcode", target = "postcode")
	@Mapping(source="dataAddress.streetNumberSide", target = "streetNumberSide")
	@Mapping(source="dataAddress.intersectionRight", target = "beetweenStreet.intersectionRight")
	@Mapping(source="dataAddress.intersectionLeft", target ="beetweenStreet.intersectionLeft")
	@Mapping(source="dataAddress.spatialRef", target ="geographicLocation.spatialRef")
	@Mapping(source="dataAddress.geometryType", target ="geographicLocation.geometryType")
	@Mapping(target="geographicLocation.geometry", expression="java(java.util.Arrays.asList(new com.telecom.geographicaddressmanagement.resource.v1.response.GeographicPoint[] {new com.telecom.geographicaddressmanagement.resource.v1.response.GeographicPoint(dataAddress.getX(), dataAddress.getY(), null)}))")	
	public GeographicAddress dataAddressToGeographicAddress(RequestGeographicAddressSearchByAddress requestSearchByAddress, DataAddress dataAddress);
	
}
