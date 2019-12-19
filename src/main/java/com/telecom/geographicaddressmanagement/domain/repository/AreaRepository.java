/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.repository;

import java.util.List;
import java.util.Set;

import com.telecom.geographicaddressmanagement.resource.v1.response.Area;
import com.telecom.geographicaddressmanagement.resource.v1.response.Type;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public interface AreaRepository {
	public List<Long> getBy(final double x, final double y, final List<String> type);
	
	public List<Type> getTypes();

	public List<Area> getAreaByNameAndType(final String name, final String type);

	public List<Area> getAreaAsFullTextSearch(final String name, final List<String> type, final Boolean fullText, final String fatherName, final String fatherType, final Integer offset, final Integer limit);
	
	public void addAreaSchema();

	public Area getAreaByTypeAndIdentification(final String type, final String identification);

}
