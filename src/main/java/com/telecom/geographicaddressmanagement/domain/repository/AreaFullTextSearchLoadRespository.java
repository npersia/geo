/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.repository;

import java.util.List;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public interface AreaFullTextSearchLoadRespository {
	public List<DataArea> byPage(final int page, final int size);

}
