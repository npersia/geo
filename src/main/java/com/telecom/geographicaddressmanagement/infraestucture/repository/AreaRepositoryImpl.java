/**
 * 
 */
package com.telecom.geographicaddressmanagement.infraestucture.repository;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.telecom.geographicaddressmanagement.domain.repository.AreaRepository;
import com.telecom.geographicaddressmanagement.infraestucture.database.PkgApiAddress;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;
import com.telecom.geographicaddressmanagement.resource.v1.response.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.infinispan.manager.EmbeddedCacheManager;

import org.infinispan.query.CacheQuery;
import org.infinispan.query.dsl.Query;
// import org.infinispan.query.Search;
// import org.infinispan.query.SearchManager;
import org.infinispan.query.dsl.QueryFactory;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

// new imports - start
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
// new imports - ends

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Repository
public class AreaRepositoryImpl implements AreaRepository {	
	private SimpleJdbcCall spDataAreaByPoint;
	private SimpleJdbcCall spTypes;

	private static final Logger logger = LoggerFactory.getLogger(AreaRepositoryImpl.class);

	// private final RemoteCache<Integer, Area> cache;
	private final RemoteCache<Integer, Area>  cache;
	private final QueryFactory defaultQueryFactory;

	@Autowired
	public AreaRepositoryImpl(DataSource dataSource, RemoteCacheManager remoteCacheManager) {
		this.spDataAreaByPoint = PkgApiAddress.createStoredProcedureGetAreasByPoint(dataSource);
		this.spTypes = PkgApiAddress.createStoredProcedureGetAreasType(dataSource);

		this.cache = remoteCacheManager.administration().getOrCreateCache("area", "default");
		this.defaultQueryFactory = Search.getQueryFactory(cache);
	}

	public void addAreaSchema(){
		
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.domain.repository.AreaRepository#getBy(double, double)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getBy(final double x, final double y, final List<String> type) {
		MapSqlParameterSource params = null;
		List<Long> result = null;
		
		params = new MapSqlParameterSource()
		        .addValue(PkgApiAddress.PARAM_X, x)
		        .addValue(PkgApiAddress.PARAM_Y, y)
		        .addValue(PkgApiAddress.PARAM_AREA_TYPE,  String.join(",", type));
		
		result = (List<Long>) spDataAreaByPoint.execute(params).get(PkgApiAddress.QUERY_RESULT_ZONES);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Type> getTypes() {		
		List<Type> result = null;
		
		result = (List<Type>) spTypes.execute().get(PkgApiAddress.QUERY_RESULT_AREA_TYPE);		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaByNameAndType(final String name, final String type) {
		List<Area> result = null;

		// org.infinispan.query.SearchManager manager = Search.getSearchManager(cache);

		// final org.hibernate.search.query.dsl.QueryBuilder queryBuilder = manager.buildQueryBuilderForClass(Area.class).get();
		
		// org.apache.lucene.search.Query secondQuery = queryBuilder
		// 	.bool()
		// 	.must( queryBuilder.keyword().onField("name").matching(name).createQuery() )
		// 	.must( queryBuilder.keyword().onField("type").matching(type).createQuery() )
		// 	.createQuery();

		// CacheQuery cacheQuery = manager.getQuery(secondQuery);

		// List<Area> matches = cacheQuery.list();

		// if (matches.size() == 0) {
		// 	logger.debug("No hay resultados" );
		// }
		// else{
		// 	result = matches;
		// 	System.out.println(matches);
		// }
		Query q = defaultQueryFactory.from(Area.class)
		.having("name").eq(name)
		.and()
		.having("type").eq(type)
		.build();
		List<Area> matches = q.list();
		
		if (matches.size() == 0) {
			logger.debug("No hay resultados" );
		}
		else{
			result = matches;
			System.out.println(matches);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaAsFullTextSearch(final String name, final List<String> type, final Boolean fullText, final String fatherName, final String fatherType, final Integer offset, final Integer limit) {
		List<Area> areasPage = null;
		String nameToQuery = "*" + name.replace(" ", "*") + "*"; 

		// org.infinispan.query.SearchManager manager = Search.getSearchManager(cache);

		// final org.hibernate.search.query.dsl.QueryBuilder queryBuilder = manager.buildQueryBuilderForClass(Area.class).get();
		
		// org.apache.lucene.search.Query secondQuery = queryBuilder
		// 	.bool()
		// 	.must( queryBuilder.keyword().wildcard().onField("name").matching(nameToQuery).createQuery() )
		// 	.should( queryBuilder.keyword().onField("fathers.name").matching(fatherName).createQuery() )
		// 	.should( queryBuilder.keyword().onField("fathers.type").matching(fatherType).createQuery() )
		// 	.createQuery();

		// CacheQuery cacheQuery = manager.getQuery(secondQuery);

		// cacheQuery.firstResult(offset);
		// cacheQuery.maxResults(limit);

		// org.apache.lucene.search.Sort sort = new Sort( 
		// 		new SortField("sortName", SortField.Type.STRING, false)
		// 	);
		// cacheQuery.sort(sort);

		// areasPage = cacheQuery.list();

		Query q = defaultQueryFactory.from(Area.class)
		.having("name").like(nameToQuery)
		// .and()
		// .having("fathers.name").eq(fatherName)
		// .and()
		// .having("fathers.type").eq(fatherType)
		.build();
		List<Area> matches = q.list();
		
		if (matches.size() == 0) {
			logger.debug("No hay resultados" );
		}
		else{
			areasPage = matches;
			System.out.println(matches);
		}
		return areasPage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Area getAreaByTypeAndIdentification(final String type, final String identification) {
		Area result = null;
		// org.infinispan.query.SearchManager manager = Search.getSearchManager(cache);
		// final org.hibernate.search.query.dsl.QueryBuilder queryBuilder = manager.buildQueryBuilderForClass(Area.class).get();
		// org.apache.lucene.search.Query query = queryBuilder
		// 	.bool()
		// 	.must( queryBuilder.keyword().onField("identification").matching(identification).createQuery() )
		// 	.must( queryBuilder.keyword().onField("type").matching(type).createQuery() )
		// 	.createQuery();
		// CacheQuery cacheQuery = manager.getQuery(query);
		// List<Area> matches = cacheQuery.list();

		// if (matches.size() == 0) {
		// 	logger.debug("No hay resultados" );
		// }
		// else{
		// 	result = matches.get(0);
		// 	System.out.println(matches);
		// }

		// get the query factory:
		// QueryFactory queryFactory = Search.getQueryFactory(cache);

		// org.infinispan.query.dsl.Query q = queryFactory.create("from Area a where a.type = '" + type + "' and " +
        //         "b.identification = '" + identification +  "'");

		// List<Area> matches = q.list();

		// if (matches.size() == 0) {
		// 	logger.debug("No hay resultados" );
		// }
		// else{
		// 	result = matches.get(0);
		// 	System.out.println(matches);
		// }

		Query q = defaultQueryFactory.from(Area.class)
		.having("type").eq(type)
		.and()
		.having("identification").eq(identification)
		.build();
		List<Area> matches = q.list();

		if (matches.size() == 0) {
			logger.debug("No hay resultados" );
		}
		else{
			result = matches.get(0);
			System.out.println(matches);
		}

		return result;
	}
}




