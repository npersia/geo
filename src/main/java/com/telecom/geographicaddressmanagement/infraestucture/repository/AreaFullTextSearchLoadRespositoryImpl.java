/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository;

import java.util.List;

import javax.sql.DataSource;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;
import com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchLoadRespository;
import com.telecom.geographicaddressmanagement.infraestucture.repository.mapper.DataAreaMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Repository
public class AreaFullTextSearchLoadRespositoryImpl implements AreaFullTextSearchLoadRespository {
	private SimpleJdbcCall spGetAreas;

	
	@Autowired
	public AreaFullTextSearchLoadRespositoryImpl(DataSource dataSource) {		
		this.spGetAreas = new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess().withCatalogName("api_area_dml")
		        .withProcedureName("get_areas")
		        .declareParameters(
		        		new SqlParameter("p_page", OracleTypes.NUMBER, "NUMBER"),
		        		new SqlParameter("p_page_size", OracleTypes.NUMBER, "NUMBER")		        		
		        )		        
		        .returningResultSet("p_areas", new DataAreaMapper());
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchLoadRespository#byPage(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataArea> byPage(final int page, final int size) {
		MapSqlParameterSource params = null;
		List<DataArea> result = null;
		
		params = new MapSqlParameterSource()
		        .addValue("p_page", page)
		        .addValue("p_page_size", size);
		
		result = (List<DataArea>) this.spGetAreas.execute(params).get("p_areas");
		
		return result;
		
		/*
		DataAreaSuggest prueba = null;
		List<DataAreaSuggest> result = new ArrayList<DataAreaSuggest>();
		
		if (page <= 1) {
			prueba = new DataAreaSuggest();
			prueba.setId(1L);
			prueba.setFathers("/1#Partido#CAPITAL FEDERAL/2#Provincias#CAPITAL FEDERAL/3#Pais#ARGENTINA");
			prueba.setName("CAPITAL FEDERAL");
			prueba.setType("Localidades");		
			result.add(prueba);
			
			prueba = new DataAreaSuggest();
			prueba.setId(4L);
			prueba.setFathers("/1#Partido#CAPITAL FEDERAL/2#Provincias#CAPITAL FEDERAL/3#Pais#ARGENTINA");
			prueba.setName("AVALOS");
			prueba.setType("Hubs");
			result.add(prueba);
			
			prueba = new DataAreaSuggest();
			prueba.setId(5L);
			prueba.setFathers("/1#Partido#JUJUY/2#Provincias#JUJUY/3#Pais#ARGENTINA");
			prueba.setName("FEDERAL");
			prueba.setType("Localidades");
			result.add(prueba);
		}
		
		return result;
		*/
	}

}
