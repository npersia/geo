CREATE OR REPLACE PACKAGE api_area_dml
  /**
  * Package:         API_AREA_DML
  * Description:     Api correspondiente al microservicio de Geographic Address Managment
  * DB impact:       NO.
  * Commit inside:   NO.
  * Rollback inside: NO.
  * @headcom
  */ 
  AS  
  /**
  * Obtiene las areas que cubren un determinado punto
  *
  * @author Pablo Delbuono - IBM
  * @param  p_page numero de pagina
  * @param  p_page_size cantidad de filas de una pagina
  * @param  p_areas datos de las areas
  * @throws N/A
  */
    PROCEDURE get_areas (
        p_page       IN        NUMBER,
        p_page_size  IN        NUMBER,        
        p_areas      OUT       SYS_REFCURSOR
    );
END api_area_dml;
/

CREATE OR REPLACE PACKAGE BODY api_area_dml AS
  
/************************************************* GET_AREAS_BY_NAME  ****************************************************/  
    PROCEDURE get_areas (
        p_page          IN        NUMBER,
        p_page_size     IN        NUMBER,        
        p_areas         OUT       SYS_REFCURSOR
    )
    IS
    BEGIN
        OPEN p_areas FOR 
        SELECT
            id_area as id,
            tipo as type,
            nombre as name,
            padres as fathers,
            propiedades as characteristics,
            identificacion as identification
        FROM (
            SELECT
                data_area.*,
                rownum as rn
            FROM (
                SELECT
                    id_area,
                    tipo,
                    nombre,
                    padres,
                    propiedades,
                    identificacion
                FROM AREA
                ORDER BY ID_AREA
            ) data_area
            WHERE
                rownum < ((p_page * p_page_size) +1)
        )
        WHERE
            rn >=  ((p_page-1)*p_page_size) + 1;
    END get_areas; 

END api_area_dml;
/