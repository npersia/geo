package com.telecom.geographicaddressmanagement.resource.v1.mapper;

import com.telecom.geographicaddressmanagement.domain.model.DataArea;
import com.telecom.geographicaddressmanagement.resource.v1.response.Area;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-18T12:46:38-0300",
    comments = "version: 1.3.0.Final, compiler: Eclipse JDT (IDE) 3.20.0.v20191115-2214, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class AreaMapperImpl implements AreaMapper {

    @Override
    public List<Area> dataAreasToAreas(List<DataArea> dataArea) {
        if ( dataArea == null ) {
            return new ArrayList<Area>();
        }

        List<Area> list = new ArrayList<Area>( dataArea.size() );
        for ( DataArea dataArea1 : dataArea ) {
            list.add( toArea( dataArea1 ) );
        }

        return list;
    }

    @Override
    public Area toArea(DataArea dataAreaSuggest) {

        Area area = new Area();

        if ( dataAreaSuggest != null ) {
        }

        return area;
    }
}
