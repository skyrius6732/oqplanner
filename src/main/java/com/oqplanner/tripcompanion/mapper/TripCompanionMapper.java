package com.oqplanner.tripcompanion.mapper;

import com.oqplanner.tripcompanion.domain.TripCompanion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripCompanionMapper {

    int saveCompanion(TripCompanion tripCompanion);
    List<TripCompanion> getCompanionList(TripCompanion tripCompanion);
    int modifyCompanionList(List<TripCompanion> tripCompanionList);
    int removeCompanion(TripCompanion tripCompainion);



}
