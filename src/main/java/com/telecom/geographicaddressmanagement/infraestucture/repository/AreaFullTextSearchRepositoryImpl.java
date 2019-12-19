/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;

//new - start
import org.springframework.cache.annotation.CacheConfig;
// new - end

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Repository
@CacheConfig(cacheNames = "area")
public class AreaFullTextSearchRepositoryImpl implements AreaFullTextSearchRepository {
	
	@Autowired
	private CacheManager cacheManager;

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getByTextSearch(java.lang.String)
	 */
	@Override
	@Cacheable(value="area", key="#suggest", unless="#result==null or #result.isEmpty()")
	public Set<Long> getByTextSearch(final String suggest) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getByType(java.lang.String)
	 */
	@Override
	@Cacheable(value="area", key="#type", unless="#result==null or #result.isEmpty()")
	public Set<Long> getByType(final String type) {
		return Collections.emptySet();		
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getByFather(java.lang.String)
	 */
	@Override
	@Cacheable(value="area", key="{#type, #name}", unless="#result==null or #result.isEmpty()")
	public Set<Long> getByFather(final String type, final String name) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getDataArea(java.lang.Long)
	 */
	@Override
	// @Cacheable(value="areafulltextarea", key="#idarea", unless="#result==null")
	@Cacheable(value="area", key="#idarea", unless="#result==null")
	public Area get(Long idarea) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getDataArea(java.lang.Long)
	 */
	@Override
	// @CachePut(value="areafulltextarea", key="#area.id", condition = "#result != null")
	@CachePut(value="area", key="#area.id", condition = "#result != null")
	public Area update(final Area area) {
		return area;
		
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getDataArea(java.lang.Long)
	 */
	@Override
	@CachePut(value="area", key="#suggest", condition = "#result != null and ! #result.isEmpty()")
	public Set<Long> updateSuggest(final String suggest, final Set<Long> areas) {
		return areas;
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getDataArea(java.lang.Long)
	 */
	@Override
	@CachePut(value="area", key="#type", condition = "#result != null and ! #result.isEmpty()")
	public Set<Long> updateAreaType(String type, Set<Long> areas) {
		return areas;
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getDataArea(java.lang.Long)
	 */
	@Override
	@CachePut(value="area", key="{#type, #name}", condition = "#result != null and ! #result.isEmpty()")
	public Set<Long> updateFather(final String type, final String name, final Set<Long> areas) {
		return areas;
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#getByTypeAndName(java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(value="area", key="{#type, #name}", unless="#result==null or #result.isEmpty()")
	public Set<Long> getByTypeAndName(final String type, final String name) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#updateAreaTypeAndName(java.lang.String, java.lang.String, com.telecom.geographicaddressmanagement.resource.v1.response.Area)
	 */
	@Override
	@CachePut(value="area", key="{#type, #name}", condition = "#result != null and ! #result.isEmpty()")
	public Set<Long> updateAreaTypeAndName(final String type, final String name, final Set<Long> idareas) {		
		return idareas;
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository#removeAll()
	 */
	@Override
	public void removeAll() {
		this.cacheManager.getCacheNames().stream().filter(name -> name.startsWith("area"))
	      .forEach(cacheName -> cacheManager.getCache(cacheName).clear());		
	}

	@Override
	@Cacheable(value="area", key="{#type, #identification}", unless="#result==null")
	public Long getByTypeAndIdentification(final String type, final String identification) {
		return null;
	}

	@Override
	@CachePut(value="area", key="{#type, #identification}", condition = "#result != null")
	public Long updateAreaTypeAndIdentification(final String type, final String identification, final Long idarea) {
		return idarea;
	}

}
