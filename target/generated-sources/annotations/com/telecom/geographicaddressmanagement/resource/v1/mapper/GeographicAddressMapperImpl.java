package com.telecom.geographicaddressmanagement.resource.v1.mapper;

import com.telecom.geographicaddressmanagement.domain.model.DataAddress;
import com.telecom.geographicaddressmanagement.domain.model.RelationArea;
import com.telecom.geographicaddressmanagement.domain.model.RelationPointOfInterest;
import com.telecom.geographicaddressmanagement.resource.v1.request.RequestGeographicAddressSearchByAddress;
import com.telecom.geographicaddressmanagement.resource.v1.response.BeetweenStreet;
import com.telecom.geographicaddressmanagement.resource.v1.response.GeographicAddress;
import com.telecom.geographicaddressmanagement.resource.v1.response.GeographicLocation;
import com.telecom.geographicaddressmanagement.resource.v1.response.RelationsArea;
import com.telecom.geographicaddressmanagement.resource.v1.response.RelationsPointOfInterest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-18T12:33:56-0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_231 (Oracle Corporation)"
)
@Component
public class GeographicAddressMapperImpl implements GeographicAddressMapper {

    @Override
    public GeographicAddress dataAddressToGeographicAddress(RequestGeographicAddressSearchByAddress requestSearchByAddress, DataAddress dataAddress) {

        GeographicAddress geographicAddress = new GeographicAddress();

        if ( requestSearchByAddress != null ) {
            geographicAddress.setCountry( requestSearchByAddress.getCountry() );
            geographicAddress.setCity( requestSearchByAddress.getCity() );
            geographicAddress.setStreetNr( requestSearchByAddress.getStreetNr() );
            geographicAddress.setLocality( requestSearchByAddress.getLocality() );
            geographicAddress.setStreetName( requestSearchByAddress.getStreetName() );
            geographicAddress.setStateOrProvince( requestSearchByAddress.getStateOrProvince() );
        }
        if ( dataAddress != null ) {
            geographicAddress.setBeetweenStreet( dataAddressToBeetweenStreet( dataAddress ) );
            geographicAddress.setGeographicLocation( dataAddressToGeographicLocation( dataAddress ) );
            geographicAddress.setStreetType( dataAddress.getStreetType() );
            geographicAddress.setPostcode( dataAddress.getPostcode() );
            geographicAddress.setStreetNumberSide( dataAddress.getStreetNumberSide() );
            geographicAddress.setZones( relationAreaListToRelationsAreaList( dataAddress.getZones() ) );
            geographicAddress.setPointsOfInterest( relationPointOfInterestListToRelationsPointOfInterestList( dataAddress.getPointsOfInterest() ) );
        }

        return geographicAddress;
    }

    protected BeetweenStreet dataAddressToBeetweenStreet(DataAddress dataAddress) {

        BeetweenStreet beetweenStreet = new BeetweenStreet();

        if ( dataAddress != null ) {
            beetweenStreet.setIntersectionRight( dataAddress.getIntersectionRight() );
            beetweenStreet.setIntersectionLeft( dataAddress.getIntersectionLeft() );
        }

        return beetweenStreet;
    }

    protected GeographicLocation dataAddressToGeographicLocation(DataAddress dataAddress) {

        GeographicLocation geographicLocation = new GeographicLocation();

        if ( dataAddress != null ) {
            geographicLocation.setGeometryType( dataAddress.getGeometryType() );
            geographicLocation.setSpatialRef( dataAddress.getSpatialRef() );
        }
        geographicLocation.setGeometry( java.util.Arrays.asList(new com.telecom.geographicaddressmanagement.resource.v1.response.GeographicPoint[] {new com.telecom.geographicaddressmanagement.resource.v1.response.GeographicPoint(dataAddress.getX(), dataAddress.getY(), null)}) );

        return geographicLocation;
    }

    protected RelationsArea relationAreaToRelationsArea(RelationArea relationArea) {

        RelationsArea relationsArea = new RelationsArea();

        if ( relationArea != null ) {
            relationsArea.setType( relationArea.getType() );
            relationsArea.setValue( relationArea.getValue() );
        }

        return relationsArea;
    }

    protected List<RelationsArea> relationAreaListToRelationsAreaList(List<RelationArea> list) {
        if ( list == null ) {
            return new ArrayList<RelationsArea>();
        }

        List<RelationsArea> list1 = new ArrayList<RelationsArea>( list.size() );
        for ( RelationArea relationArea : list ) {
            list1.add( relationAreaToRelationsArea( relationArea ) );
        }

        return list1;
    }

    protected RelationsPointOfInterest relationPointOfInterestToRelationsPointOfInterest(RelationPointOfInterest relationPointOfInterest) {

        RelationsPointOfInterest relationsPointOfInterest = new RelationsPointOfInterest();

        if ( relationPointOfInterest != null ) {
            relationsPointOfInterest.setType( relationPointOfInterest.getType() );
            relationsPointOfInterest.setId( relationPointOfInterest.getId() );
        }

        return relationsPointOfInterest;
    }

    protected List<RelationsPointOfInterest> relationPointOfInterestListToRelationsPointOfInterestList(List<RelationPointOfInterest> list) {
        if ( list == null ) {
            return new ArrayList<RelationsPointOfInterest>();
        }

        List<RelationsPointOfInterest> list1 = new ArrayList<RelationsPointOfInterest>( list.size() );
        for ( RelationPointOfInterest relationPointOfInterest : list ) {
            list1.add( relationPointOfInterestToRelationsPointOfInterest( relationPointOfInterest ) );
        }

        return list1;
    }
}
