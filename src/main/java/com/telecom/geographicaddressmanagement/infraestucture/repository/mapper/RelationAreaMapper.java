/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecom.geographicaddressmanagement.domain.model.RelationArea;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public class RelationAreaMapper implements RowMapper<RelationArea> {

	@Override
	public RelationArea mapRow(ResultSet rs, int rownum) throws SQLException {
		RelationArea result = null;
		
		result = new RelationArea();
		result.setType(rs.getString("type"));
		result.setValue(rs.getString("value"));
		
		return result;
	}	 
}
