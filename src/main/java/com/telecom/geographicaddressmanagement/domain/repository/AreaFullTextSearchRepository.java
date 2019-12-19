/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.repository;

import java.util.Set;

import com.telecom.geographicaddressmanagement.resource.v1.response.Area;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public interface AreaFullTextSearchRepository {
	public Set<Long> getByTextSearch(final String suggest);
	public Set<Long> getByType(final String type);
	public Set<Long> getByFather(final String type, final String name);
	public Area get(final Long idarea);
	public Set<Long> getByTypeAndName(final String type, final String name);
	public Long getByTypeAndIdentification(final String type, final String identification);

	public Area update(final Area area);
	public Set<Long> updateSuggest(final String suggest, final Set<Long> areas);
	public Set<Long> updateAreaType(final String type, final Set<Long> areas);
	public Set<Long> updateFather(final String type, final String name, final Set<Long> areas);
	public Set<Long> updateAreaTypeAndName(final String type, final String name, final Set<Long> idareas);
	public Long updateAreaTypeAndIdentification(final String type, final String identification, final Long idarea);
	
	public void removeAll();
	
}
