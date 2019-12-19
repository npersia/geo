/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public class DataAreaMapper implements RowMapper<DataArea> {

	@Override
	public DataArea mapRow(ResultSet rs, int rownum) throws SQLException {
		DataArea result = null;
				
		result = new DataArea();		
		result.setId(rs.getLong("id"));
		result.setName(rs.getString("name"));
		result.setType(rs.getString("type"));
		result.setFathers(rs.getString("fathers"));
		result.setCharacteristics(rs.getString("characteristics"));
		result.setIdentification(rs.getString("identification"));
				
		return result;
	}

}
