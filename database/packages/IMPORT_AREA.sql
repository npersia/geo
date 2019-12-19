CREATE OR REPLACE PACKAGE IMPORT_AREA AS
    PROCEDURE IMPORT;
    FUNCTION GET_IDENTIFICATION_AREA(p_fathers IN VARCHAR2, p_name VARCHAR2) RETURN AREA.PADRES%TYPE;
    FUNCTION GET_PROPIEDADES_AREA(p_idarea IN AREA.ID_AREA%TYPE) RETURN AREA.PROPIEDADES%TYPE;
END IMPORT_AREA;
/

CREATE OR REPLACE PACKAGE BODY IMPORT_AREA
AS 
    c_pattern_list_parameter            CONSTANT VARCHAR2(10) := '[^;]+';
    c_pattern_father_parameter          CONSTANT VARCHAR2(10) := '[^#]+';
    c_separator_list_id                 CONSTANT VARCHAR2(10) := ',';
    c_separator_list_parameter          CONSTANT VARCHAR2(10) := ';';
    c_separator_list_father             CONSTANT VARCHAR2(10) := '#';
   
    -------------------------------------------------------------------------------------------        
    PROCEDURE MERGE_AREAS(p_id_lote_importacion IN AREA.ID_LOTE_IMPORTACION%TYPE, p_area IN TT_AREA);
    PROCEDURE DELETE_ALL(p_id_lote_importacion IN AREA.ID_LOTE_IMPORTACION%TYPE);
    PROCEDURE GET_PROVINCIAS(p_provincias  OUT NOCOPY TT_AREA);
    PROCEDURE GET_PARTIDOS(p_partidos  OUT NOCOPY TT_AREA); 
    PROCEDURE GET_LOCALIDADES(p_localidades  OUT NOCOPY TT_AREA);   
    PROCEDURE GET_BARRIOS(p_barrios  OUT NOCOPY TT_AREA); 
    PROCEDURE GET_AREA_NO_POLITICA(p_area  OUT NOCOPY TT_AREA);     

    -----------------------------------------------------------------------------------------------------------------------
    FUNCTION GET_PROPIEDADES_AREA(p_idarea IN AREA.ID_AREA%TYPE) RETURN AREA.PROPIEDADES%TYPE
    IS
        v_result AREA.PROPIEDADES%TYPE;
    BEGIN
        SELECT propiedades
        INTO v_result
        FROM (
            SELECT
            ap.id_area,
            LISTAGG(apd.descripcion || c_separator_list_father ||ap.valor, c_separator_list_parameter) WITHIN GROUP(ORDER BY ap.id_prop) as propiedades
            FROM area_prop ap
            JOIN area_prop_def apd ON (apd.id_prop = ap.id_prop)
            where ap.id_area = p_idarea
            group by id_area
        );
        
        RETURN v_result;
    END GET_PROPIEDADES_AREA;
    
    -----------------------------------------------------------------------------------------------------------------------
    FUNCTION GET_IDENTIFICATION_AREA(p_fathers IN VARCHAR2, p_name VARCHAR2) RETURN AREA.PADRES%TYPE
    IS
        v_count     SMALLINT;
        v_result    AREA.PADRES%TYPE;
        
    BEGIN
        v_count := regexp_count(p_fathers, c_separator_list_parameter) + 1;
        
        SELECT identificador
        INTO v_result
        FROM (
            SELECT 
            nombre,
            LISTAGG(fathers_name, c_separator_list_id) WITHIN GROUP (ORDER BY orden desc) || c_separator_list_id || nombre AS identificador
            FROM (
                SELECT 
                    p_name as nombre,
                    regexp_substr(
                        TRIM(regexp_substr(p_fathers, c_pattern_list_parameter, 1, level)),c_pattern_father_parameter,1) as orden,
                        regexp_substr(
                            TRIM(regexp_substr(p_fathers, c_pattern_list_parameter, 1, level)) 
                            ,c_pattern_father_parameter,
                            1,
                            3
                        ) as fathers_name
                FROM DUAL
                CONNECT BY level <= v_count 
            )
            group by nombre
        );    
        
        RETURN v_result;
    END GET_IDENTIFICATION_AREA;
        
    -------------------------------------------------------------------------------------------        
    PROCEDURE IMPORT
    IS
        v_id_lote_importacion    AREA.ID_LOTE_IMPORTACION%TYPE;
     --   v_localidades            TT_AREA;
        v_area           TT_AREA;
    BEGIN
        v_id_lote_importacion := to_number(to_char(CURRENT_TIMESTAMP,'YYYYMMDDHH24MISS'));
                
        GET_PROVINCIAS(p_provincias => v_area);
        MERGE_AREAS(p_id_lote_importacion => v_id_lote_importacion, p_area => v_area);
        v_area.delete;
        
        GET_PARTIDOS(p_partidos => v_area);
        MERGE_AREAS(p_id_lote_importacion => v_id_lote_importacion, p_area => v_area);
        v_area.delete;
        
        GET_LOCALIDADES(p_localidades  => v_area);        
        MERGE_AREAS(p_id_lote_importacion => v_id_lote_importacion, p_area => v_area);
        v_area.delete;
        
        GET_BARRIOS(p_barrios  => v_area);        
        MERGE_AREAS(p_id_lote_importacion => v_id_lote_importacion, p_area => v_area);
        v_area.delete;
        
        GET_AREA_NO_POLITICA(p_area => v_area);
        MERGE_AREAS(p_id_lote_importacion => v_id_lote_importacion, p_area => v_area);
        v_area.delete;
                
        DELETE_ALL(p_id_lote_importacion => v_id_lote_importacion);
        COMMIT;
    END IMPORT;
    
    -------------------------------------------------------------------------------------------    
    PROCEDURE GET_LOCALIDADES(p_localidades OUT NOCOPY TT_AREA)
    IS
    BEGIN    
        SELECT T_AREA (
            localidad.id_area,
            tipo_localidad .nom_grp_tipo_area,
            localidad.nombre,            
            ';1#' || tipo_partido.nom_grp_tipo_area   || '#'|| partido.nombre    ||    
            ';2#' || tipo_provincia.nom_grp_tipo_area || '#'|| provincia.nombre  || 
            ';3#' || tipo_pais.nom_grp_tipo_area      || '#'|| pais.nombre   ,
            null
        )
        BULK COLLECT INTO p_localidades
        FROM GISUM_AREA localidad
        JOIN GISUM_GIS_GRP_TIPO_AREA tipo_localidad ON (localidad.fk_tipo = tipo_localidad.id_tipo_area)
        JOIN GISUM_AREA partido ON ( partido.id_area = localidad.fk_padre )
        JOIN GISUM_GIS_GRP_TIPO_AREA tipo_partido ON (partido.fk_tipo = tipo_partido.id_tipo_area)
        JOIN GISUM_AREA provincia ON ( substr(partido.alias, 4) = provincia.alias)
        JOIN GISUM_GIS_GRP_TIPO_AREA tipo_provincia ON (provincia.fk_tipo = tipo_provincia.id_tipo_area)
        JOIN GISUM_AREA pais ON ( pais.id_area = provincia.fk_padre )
        JOIN GISUM_GIS_GRP_TIPO_AREA tipo_pais ON (pais.fk_tipo = tipo_pais.id_tipo_area)
        WHERE
            localidad.fk_tipo = 8
            AND provincia.fk_tipo = 47 ;                
    END GET_LOCALIDADES ;
    
    -------------------------------------------------------------------------------------------    
    PROCEDURE MERGE_AREAS(p_id_lote_importacion IN AREA.ID_LOTE_IMPORTACION%TYPE, p_area IN TT_AREA)
    IS
    BEGIN
        MERGE INTO AREA target 
        USING (        
             SELECT
                id_Area,
                tipo,
                nombre,
                padres,
                propiedades
                from TABLE(CAST( p_area AS TT_AREA)) 
        ) source
        ON (target.id_area = source.id_area)
        WHEN MATCHED THEN 
        UPDATE SET 
           target.ID_LOTE_IMPORTACION   = p_id_lote_importacion,
           target.FECHAMIGRAULT         = SYSDATE,
           target.nombre                = source.nombre,
           target.padres                = source.padres,
           target.propiedades           = IMPORT_AREA.GET_PROPIEDADES_AREA(p_idarea => source.id_area),
           target.identificacion        = IMPORT_AREA.GET_IDENTIFICATION_AREA(p_fathers => source.padres, p_name => source.nombre)
        WHEN NOT MATCHED THEN 
        INSERT (
            ID_AREA,
            TIPO,
            NOMBRE,
            PADRES,
            PROPIEDADES,
            FECHAMIGRAINI,
            FECHAMIGRAULT,
            FECHAMIGRAFIN,
            ID_LOTE_IMPORTACION,
            IDENTIFICACION
        ) 
        VALUES (
            source.id_area,
            source.tipo,
            source.nombre,
            source.padres,
            source.propiedades,
            SYSDATE,
            SYSDATE,
            IMPORT_AREA.GET_PROPIEDADES_AREA(p_idarea => source.id_area),
            p_id_lote_importacion,
            IMPORT_AREA.GET_IDENTIFICATION_AREA(p_fathers => source.padres, p_name => source.nombre)
        )
        LOG ERRORS INTO err$_area ('IMPORT') REJECT LIMIT UNLIMITED;
    END MERGE_AREAS;
    
    -------------------------------------------------------------------------------------------    
    PROCEDURE DELETE_ALL(p_id_lote_importacion AREA.ID_LOTE_IMPORTACION%TYPE)
    IS
    BEGIN
        DELETE FROM AREA WHERE ID_LOTE_IMPORTACION < p_id_lote_importacion;    
    END;
   
   -------------------------------------------------------------------------------------------
    PROCEDURE GET_AREA_NO_POLITICA(p_area  OUT NOCOPY TT_AREA)
    IS
    BEGIN                
        SELECT T_AREA (
            id_area,
            tipo,
            nombre,
            padres,
            propiedades
        )
        BULK COLLECT INTO p_area
        FROM (
            SELECT
                id_area as id_area,
                tipo as tipo,
                nombre as nombre,            
                replace(padres, ';0#'||tipo||'#'||nombre) as padres,
                null as propiedades
            FROM (
                SELECT
                    CONNECT_BY_ROOT id_area                                                             AS id_area,
                    CONNECT_BY_ROOT nombre                                                              AS Nombre,
                    CONNECT_BY_ROOT tipo.nom_grp_tipo_area                                              AS Tipo,
                    SYS_CONNECT_BY_PATH(level-1 || '#' || tipo.nom_grp_tipo_area || '#' || nombre,';')  AS padres,
                    CONNECT_BY_ISLEAF IsLeaf
                FROM GISUM_AREA area
                JOIN GISUM_GIS_GRP_TIPO_AREA tipo ON (area.fk_tipo = tipo.id_tipo_area)
                WHERE
                    CONNECT_BY_ISLEAF = 1
                    AND id_area > 0
                START WITH fk_tipo NOT IN (47,5,8,9)
                CONNECT BY ((PRIOR fk_padre = id_area) and (fk_padre is not null) and (id_area <> fk_padre))
            )
        );  
    END GET_AREA_NO_POLITICA ;
    
    -------------------------------------------------------------------------------------------
     PROCEDURE GET_PROVINCIAS(p_provincias  OUT NOCOPY TT_AREA)
     IS
     BEGIN
         SELECT T_AREA(
            id_area,
            tipo,
            nombre,
            padres,
            propiedades
        )
        BULK COLLECT INTO p_provincias
        FROM (
            SELECT   
                provincia.id_area                                                   AS id_area,
                provincia.nombre                                                    AS nombre,
                tipo_provincia .nom_grp_tipo_area                                   AS tipo,
                ';1#' || tipo_pais.nom_grp_tipo_area      || '#'|| pais.nombre      AS padres,
                null                                                                AS propiedades
            FROM GISUM_AREA provincia
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_provincia ON (provincia.fk_tipo = tipo_provincia.id_tipo_area)
            JOIN GISUM_AREA pais ON ( pais.id_area = provincia.fk_padre )
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_pais ON (pais.fk_tipo = tipo_pais.id_tipo_area)
            WHERE
                provincia.fk_tipo = 47
        );
     END GET_PROVINCIAS;     
     
    -------------------------------------------------------------------------------------------
     PROCEDURE GET_PARTIDOS(p_partidos  OUT NOCOPY TT_AREA)
     IS
     BEGIN
         SELECT T_AREA(
            id_area,
            tipo,
            nombre,
            padres,
            propiedades
        )
        BULK COLLECT INTO p_partidos
        FROM (
            SELECT   
                partido.id_area                                                             AS id_area,                                                             
                partido.nombre                                                              AS nombre,
                tipo_partido .nom_grp_tipo_area                                             AS tipo,   
                ';1#' || tipo_provincia.nom_grp_tipo_area || '#'|| provincia.nombre  ||
                ';2#' || tipo_pais.nom_grp_tipo_area      || '#'|| pais.nombre              AS padres,
                null                                                                        AS propiedades
            FROM GISUM_AREA partido 
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_partido ON (partido.fk_tipo = tipo_partido.id_tipo_area)
            JOIN GISUM_AREA provincia ON ( substr(partido.alias, 4) = provincia.alias)
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_provincia ON (provincia.fk_tipo = tipo_provincia.id_tipo_area)
            JOIN GISUM_AREA pais ON ( pais.id_area = provincia.fk_padre )
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_pais ON (pais.fk_tipo = tipo_pais.id_tipo_area)
            WHERE
                partido.fk_tipo = 5
                AND provincia.fk_tipo = 47
        );
     END GET_PARTIDOS;   
     
    -------------------------------------------------------------------------------------------
     PROCEDURE GET_BARRIOS(p_barrios  OUT NOCOPY TT_AREA)
     IS
     BEGIN
         SELECT T_AREA(
            id_area,
            tipo,
            nombre,
            padres,
            propiedades
        )
        BULK COLLECT INTO p_barrios
        FROM (
            SELECT   
                barrio.id_area                                                              AS id_area,
                barrio.nombre                                                               AS nombre,
                tipo_barrio .nom_grp_tipo_area                                              AS tipo,
                ';1#' || tipo_localidad.nom_grp_tipo_area || '#'|| localidad.nombre  ||
                ';2#' || tipo_partido.nom_grp_tipo_area   || '#'|| partido.nombre    ||    
                ';3#' || tipo_provincia.nom_grp_tipo_area || '#'|| provincia.nombre  || 
                ';4#' || tipo_pais.nom_grp_tipo_area      || '#'|| pais.nombre               AS padres,
                null                                                                         AS propiedades
            FROM GISUM_AREA barrio
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_barrio ON (barrio.fk_tipo = tipo_barrio.id_tipo_area)
            JOIN GISUM_AREA localidad ON ( localidad.id_area = barrio.fk_padre )
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_localidad ON (localidad.fk_tipo = tipo_localidad.id_tipo_area)
            JOIN GISUM_AREA partido ON ( partido.id_area = localidad.fk_padre )
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_partido ON (partido.fk_tipo = tipo_partido.id_tipo_area)
            JOIN GISUM_AREA provincia ON ( substr(partido.alias, 4) = provincia.alias)
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_provincia ON (provincia.fk_tipo = tipo_provincia.id_tipo_area)
            JOIN GISUM_AREA pais ON ( pais.id_area = provincia.fk_padre )
            JOIN GISUM_GIS_GRP_TIPO_AREA tipo_pais ON (pais.fk_tipo = tipo_pais.id_tipo_area)
            WHERE
                barrio.fk_tipo = 9
                AND provincia.fk_tipo = 47
        );
     END GET_BARRIOS;     
     
    -------------------------------------------------------------------------------------------
END IMPORT_AREA;
/