/**
 * 
 */
package com.telecom.geographicaddressmanagement.application.service.impl;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.telecom.geographicaddressmanagement.application.service.AreaCacheService;
import com.telecom.geographicaddressmanagement.domain.model.DataArea;
import com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchLoadRespository;
import com.telecom.geographicaddressmanagement.domain.repository.AreaFullTextSearchRepository;

import com.telecom.geographicaddressmanagement.resource.v1.mapper.AreaMapper;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;

// import org.infinispan.manager.EmbeddedCacheManager;
// import org.infinispan.manager.DefaultCacheManager;
// new imports - start
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
// new imports - ends

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Service
public class AreaCacheServiceImpl implements AreaCacheService {
	
	private static final Logger logger = LoggerFactory.getLogger(AreaCacheServiceImpl.class);

	@Autowired
	private AreaFullTextSearchRepository repositoryCache;
	
	@Autowired
	private AreaFullTextSearchLoadRespository repositoryDB;
	
	@Autowired
	private AreaMapper areaMapper;
	
	@Value("${cache.area.fastload:false}")
	private boolean cacheAreaCargaRapida;
	
	@Value("${cache.area.page.size:100000}")
	private int sizePage;
	
	// private final EmbeddedCacheManager cacheManager;
	private final RemoteCache<Integer, String> cache;

	@Autowired
	public AreaCacheServiceImpl(RemoteCacheManager remoteCacheManager) {
		// this.cacheManager = cacheManager;
		this.cache = remoteCacheManager.administration().getOrCreateCache("area", "default");
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.application.service.AreaCacheService#load()
	 */
	@Override
	public void load() {
		List<DataArea> dataAreas = null;
		Set<Area> areas = null;
		int page = 1;

		logger.info("Comenzando la carga de la cache de areas");
		logger.debug("Parametros de carga: carga la primera pagina solamente {},  tamanio de pagina {}", cacheAreaCargaRapida, sizePage);
		do {
			logger.info("Comenzando a buscar las areas a la base de datos");
			dataAreas = this.repositoryDB.byPage(page, sizePage);
			logger.info("Finalizando a buscar las areas a la base de datos");

			if (! CollectionUtils.isEmpty(dataAreas)) {
				areas = this.loadDataArea(dataAreas);

				for(Area area : areas) {
					// cacheManager.getCache("area").put(area.getId(), area);
					cache.put(area.getId(), area);
				}

				// this.loadSuggestData(areas);
				// this.loadAreaTypeData(areas);
				// this.loadFatherData(areas);
				// this.loadAreabyTypeAndName(areas);
				// this.loadAreabyTypeAndIdentificaton(areas);
			}
			page++;
			
		} while ((! cacheAreaCargaRapida) && (! CollectionUtils.isEmpty(dataAreas)));
		
		logger.info("Finalizando la carga de la cache de areas");
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.application.service.AreaCacheService#removeAll()
	 */
	@Override
	public void removeAll() {
		logger.info("Comenzando a borrar la cache de areas");
		this.repositoryCache.removeAll();	
		logger.info("Finalizando a borrar la cache de areas");
	}

	/* (non-Javadoc)
	 * @see com.telecom.geographicaddressmanagement.application.service.AreaCacheService#reLoad()
	 */
	@Override
	public void reLoad() {
		logger.debug("Comenzando a recargar la cache de areas");
		this.removeAll();
		this.load();
		logger.debug("Finalizando de recargar la cache de areas");
	}
	
	/**
	 * @param areas
	 */
	private void loadAreaTypeData(final Set<Area> areas) {	
		logger.info("Comenzando a clasificar por tipo de area");
		areas.stream().parallel().collect(Collectors.groupingBy(Area::getType, Collectors.mapping(Area::getId, Collectors.toSet())))
		.entrySet().stream().forEach(areasByType -> 
		{
			Set<Long> idAreas;

			idAreas = this.repositoryCache.getByType(areasByType.getKey().toUpperCase());
			if (idAreas.isEmpty()) {
				idAreas = new HashSet<Long>();
			}
			idAreas.addAll(areasByType.getValue());
			this.repositoryCache.updateAreaType(areasByType.getKey().toUpperCase(), idAreas);
			
		});		
		logger.info("Finaliznado de clasificar por tipo de area");
	}
	
	/**
	 * @param areas
	 */
	private void loadAreabyTypeAndName(final Set<Area> areas) {
		logger.info("Comenzando a clasificar por tipo y nombre de area");
		areas.stream().parallel().collect(
				Collectors.groupingBy(
						area ->
						new AbstractMap.SimpleEntry<>(area.getType() ,area.getName())
						,Collectors.mapping(Area::getId, Collectors.toSet())
			)
		).entrySet().parallelStream().forEach(areaTypeName -> {
			Set<Long> idAreas;
			
			idAreas = this.repositoryCache.getByTypeAndName(areaTypeName.getKey().getKey(), areaTypeName.getKey().getValue());
			if (idAreas.isEmpty()) {
				idAreas = new HashSet<Long>();
			}
			idAreas.addAll(areaTypeName.getValue());			
			this.repositoryCache.updateAreaTypeAndName(areaTypeName.getKey().getKey().toUpperCase(), areaTypeName.getKey().getValue(), idAreas);
		});		
		logger.info("Finalizando de clasificar por tipo y nombre de area");
	}
	
	/**
	 * @param areas
	 */
	private void loadAreabyTypeAndIdentificaton(final Set<Area> areas) {
		logger.info("Comenzando a clasificar por tipo y identificacion de area");
		areas.stream().parallel().forEach(area -> {
			this.repositoryCache.updateAreaTypeAndIdentification(area.getType().toUpperCase(), area.getIdentification(), area.getId());
		});
		logger.info("Finalizando de clasificar por tipo y identificacion de area");
	}

	private void loadFatherData(final Set<Area> areas) {
		logger.info("Comenzando a clasificar por padre de area");
		areas.stream().parallel().flatMap(
				area -> 
				area.getFathers().stream().map(
						father -> 
						new AbstractMap.SimpleEntry<>(father, area.getId())
					)
				)
		.collect(
			Collectors.groupingBy(
				father -> 
				father.getKey(), 
				Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
				)
			)
		.entrySet().parallelStream().forEach(father -> {
			Set<Long> idAreas = null;
			
			idAreas = this.repositoryCache.getByFather(father.getKey().getType().toUpperCase(), father.getKey().getName());
			if (idAreas.isEmpty()) {
				idAreas = new HashSet<Long>();
			}
			idAreas.addAll(father.getValue());		
			this.repositoryCache.updateFather(father.getKey().getType().toUpperCase(), father.getKey().getName(), idAreas);
		});
		logger.info("Finalizando de clasificar por padre de area");
		
		
	}
	
	private void loadSuggestData(final Set<Area> areas) {	
		Map<String, Set<Long>> suggests = null;
		String suggest;
		
		logger.info("Comenzando a clasificar por suggest de area");
		//Queda mas complejo hacerlo con stream
		suggests = new HashMap<>();
		for(Area area : areas) {
			for (String token : StringUtils.tokenizeToStringArray(area.getName(), " ")) {
				if(token.length() > 2 ) {
					for (int index = 3; index <= token.length(); index++ ) {
						suggest = token.substring(0, index);
						if (suggests.containsKey(suggest)) {
							suggests.get(suggest).add(area.getId());
						} else {
							suggests.put(suggest, new HashSet<>(Arrays.asList(area.getId())));
						}
					}
				}
			}
		}
				
		suggests.entrySet().parallelStream().forEach(entry -> {
			Set<Long> idAreasSuggests = null;
			
			idAreasSuggests = this.repositoryCache.getByTextSearch(entry.getKey());
			if (idAreasSuggests.isEmpty()) {
				idAreasSuggests = new HashSet<>();
			}
			idAreasSuggests.addAll(entry.getValue());
			this.repositoryCache.updateSuggest(entry.getKey(), idAreasSuggests);
			
		});
		logger.info("Finalizando clasificar por suggest de area");

	}	

	private Set<Area> loadDataArea(final List<DataArea> areas) {
		Set<Area> result = null;
		
		result = areas.stream().map(areaMapper::toArea).collect(Collectors.toSet());
		result.forEach(repositoryCache::update);

		return result;

	}	
	
//	public static void main(String[] args) {
//		String cadena = ";0#Regiones#3 LITORAL;1#Pais#ARGENTINA";
//		
//		for(String linea : cadena.replaceFirst(";", "").split(";")) {
//			System.out.println(linea);
//		}
//	}

}
