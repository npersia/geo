/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.telecom.geographicaddressmanagement.domain.model.DataAddress;
import com.telecom.geographicaddressmanagement.domain.model.RelationArea;
import com.telecom.geographicaddressmanagement.domain.model.RelationPointOfInterest;
import com.telecom.geographicaddressmanagement.domain.repository.GeographicAddressRepository;
import com.telecom.geographicaddressmanagement.infraestucture.database.PkgApiAddress;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestGeographicAddressSearchByAddress;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Repository
public class GeographicAddressRepositoryImpl implements GeographicAddressRepository {
	private static final Logger logger = LoggerFactory.getLogger(GeographicAddressRepositoryImpl.class);

	private SimpleJdbcCall sp;

	@Autowired
	public GeographicAddressRepositoryImpl(DataSource dataSource) {
		this.sp = PkgApiAddress.createStoredProcedureGetDataAddress(dataSource);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DataAddress getDataAddressBy(RequestGeographicAddressSearchByAddress address) {
		List<DataAddress> resultList = null;
		DataAddress result = null;	
		List<RelationArea> zones = null;
		Map<String, ?> queryResponse = null;
		MapSqlParameterSource params = null;
		List<RelationPointOfInterest> pointsOfIneterest = null;
		
		params = new MapSqlParameterSource()
        .addValue("p_country", address.getCountry())
        .addValue("p_stateOrProvince", address.getStateOrProvince())
        .addValue("p_city", address.getCity())
        .addValue("p_locality", address.getLocality())
        .addValue("p_streetName", address.getStreetName())
        .addValue("p_streerNr", address.getStreetNr());
        
		
		queryResponse = sp.execute(params);		
		logger.debug("Resultado del query para obtener datos de la vereda {}", queryResponse);
		
		if (queryResponse.get("p_dataAddress") != null) {
			resultList = (List<DataAddress>)queryResponse.get("p_dataAddress");
			if (! CollectionUtils.isEmpty(resultList)) {				
				if (resultList.size() > 1) {
					throw new RuntimeException("Devolvio mas de un resultado a obtener datos de la vereda");
				} else {
					result = resultList.get(0);
					
					zones = (List<RelationArea>)queryResponse.get("p_zones");
					result.setZones(zones);
					pointsOfIneterest = (List<RelationPointOfInterest>)queryResponse.get("p_pointOfInterest");
					result.setPointsOfInterest(pointsOfIneterest);
					
					result.setZones(zones);
				}
			}
		}
		logger.debug("*Resultado del query para obtener datos de la vereda despues de orquestar las llamadas{}", result);
		return result;
	}

}
