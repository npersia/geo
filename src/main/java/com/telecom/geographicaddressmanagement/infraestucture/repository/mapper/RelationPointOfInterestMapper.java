/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecom.geographicaddressmanagement.domain.model.RelationPointOfInterest;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public class RelationPointOfInterestMapper implements RowMapper<RelationPointOfInterest> {

	@Override
	public RelationPointOfInterest mapRow(ResultSet rs, int rownum) throws SQLException {
		RelationPointOfInterest result = null;
		
		result = new RelationPointOfInterest();
		result.setId(rs.getString("id"));
		result.setType(rs.getString("type"));
		
		return result;
	}

}
