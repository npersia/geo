/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.model;

import java.util.List;

import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
public class DataAddress {
	private String country;	
		
	private String stateOrProvince;
	
	private String city;
	
	private String locality;
	
	private String streetName;
	
	private Integer streetNr;
	
	private String streetType;
	
	private String postcode;
	
	private String streetNumberSide;
	
	private String intersectionLeft;
	
	private String intersectionRight;
	
	private String spatialRef;
	
	private String geometryType;
		
	private String x;
	
	private String y;
	
	private List<RelationArea> zones;
	
	private List<RelationPointOfInterest> pointsOfInterest;
}
