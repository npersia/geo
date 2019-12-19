/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.database;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.telecom.geographicaddressmanagement.infraestucture.repository.mapper.DataAddressMapper;
import com.telecom.geographicaddressmanagement.infraestucture.repository.mapper.RelationAreaMapper;
import com.telecom.geographicaddressmanagement.infraestucture.repository.mapper.RelationPointOfInterestMapper;
import com.telecom.geographicaddressmanagement.resource.v1.response.Type;

import oracle.jdbc.OracleTypes;

/**
 * @author PABLODANIELDELBUONO
 *
 */
public class PkgApiAddress {
	private static final String PKG_NAME_ADDRESS = "API_ADDRESS_SEGMENT";
	private static final String PKG_NAME_AREA = "API_ADDRESS_AREA";
	private static final String STORED_PROCEDURE_GET_DATA_ADDRESS = "GET_DATA_ADDRESS";
	private static final String STORED_PROCEDURE_GET_AREAS_BY_POINT = "GET_AREAS_BY_POINT";
//	private static final String STORED_PROCEDURE_GET_AREAS_BY_NAME = "GET_AREAS_BY_NAME";
	private static final String STORED_PROCEDURE_GET_AREAS_TYPE = "GET_AREAS_TYPE";
		
	public static final String QUERY_RESULT_ZONES = "p_zones";
	public static final String QUERY_RESULT_AREA_TYPE = "p_types";
	public static final String PARAM_AREA_TYPE = "p_type";
	public static final String PARAM_X = "p_x";
	public static final String PARAM_Y = "p_y";
	public static final String PARAM_AREA_NAME = "p_name";
	
	
	public static final SimpleJdbcCall createStoredProcedureGetDataAddress(final DataSource dataSource) {
		SimpleJdbcCall result = null;

		result = new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess().withCatalogName(PKG_NAME_ADDRESS)
		        .withProcedureName(STORED_PROCEDURE_GET_DATA_ADDRESS)
		        .declareParameters(
		        		new SqlParameter("p_country", OracleTypes.VARCHAR, "VARCHAR"),
		        		new SqlParameter("p_stateorprovince", OracleTypes.VARCHAR, "VARCHAR"),
		        		new SqlParameter("p_city", OracleTypes.VARCHAR, "VARCHAR"),
		        		new SqlParameter("p_locality", OracleTypes.VARCHAR, "VARCHAR"),
		        		new SqlParameter("p_streetname", OracleTypes.VARCHAR, "VARCHAR"),
		        		new SqlParameter("p_streernr", OracleTypes.INTEGER, "INTEGER")
		        )
		        .returningResultSet("p_dataAddress", new DataAddressMapper())
		        .returningResultSet("p_zones", new RelationAreaMapper())
		        .returningResultSet("p_pointOfInterest", new RelationPointOfInterestMapper());

		
		return result;	
	}
	
	public static final SimpleJdbcCall createStoredProcedureGetAreasByPoint(final DataSource dataSource) {
		SimpleJdbcCall result = null;

		result = new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess().withCatalogName(PKG_NAME_AREA)
		        .withProcedureName(STORED_PROCEDURE_GET_AREAS_BY_POINT)
		        .declareParameters(
		        		new SqlParameter(PARAM_X, OracleTypes.NUMBER, "NUMBER"),
		        		new SqlParameter(PARAM_Y, OracleTypes.NUMBER, "NUMBER"),
		        		new SqlParameter(PARAM_AREA_TYPE, OracleTypes.VARCHAR, "VARCHAR")
		        )		        
		        //.returningResultSet(QUERY_RESULT_ZONES, new DataAreaMapper());
		        .returningResultSet(QUERY_RESULT_ZONES, new SingleColumnRowMapper<Long>(Long.class));
		
		return result;	
	}
	
	public static final SimpleJdbcCall createStoredProcedureGetAreasType(final DataSource dataSource) {
		SimpleJdbcCall result = null;

		result = new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess().withCatalogName(PKG_NAME_AREA)
		        .withProcedureName(STORED_PROCEDURE_GET_AREAS_TYPE)
		        .returningResultSet(QUERY_RESULT_AREA_TYPE, BeanPropertyRowMapper.newInstance(Type.class));
		    
		return result;	
	}
	
//	public static final SimpleJdbcCall createStoredProcedureGetAreasByName(final DataSource dataSource) {
//		SimpleJdbcCall result = null;
//
//		result = new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess().withCatalogName(PKG_NAME)
//		        .withProcedureName(STORED_PROCEDURE_GET_AREAS_BY_NAME)
//		        .declareParameters(
//		        		new SqlParameter(PARAM_AREA_NAME, OracleTypes.VARCHAR, "VARCHAR")
//		        )		        
//		        .returningResultSet(QUERY_RESULT_ZONES, new DataAreaMapper());
//		    
//		return result;	
//	}
}
