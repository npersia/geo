/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecom.geographicaddressmanagement.domain.model.DataAddress;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public class DataAddressMapper implements RowMapper<DataAddress> {

	@Override
	public DataAddress mapRow(ResultSet rs, int rownum) throws SQLException {
		DataAddress result = null;
		
		result = new DataAddress();
		result.setStreetType(rs.getString("streettype"));
		result.setPostcode(rs.getString("postcode"));
		result.setStreetNumberSide(rs.getString("streetnumberside"));
		result.setIntersectionLeft(rs.getString("intersectionleft"));
		result.setIntersectionRight(rs.getString("intersectionright"));
        result.setX(rs.getString("x"));
        result.setY(rs.getString("y"));
        result.setSpatialRef(rs.getString("spatialref"));
        result.setGeometryType(rs.getString("geometrytype"));

		return result;
	}

}
