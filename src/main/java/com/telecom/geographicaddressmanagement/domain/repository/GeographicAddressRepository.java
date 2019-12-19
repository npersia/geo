/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.repository;

import com.telecom.geographicaddressmanagement.domain.model.DataAddress;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestGeographicAddressSearchByAddress;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public interface GeographicAddressRepository {
	public DataAddress getDataAddressBy(RequestGeographicAddressSearchByAddress address);

}
