package com.oqplanner.tripranking.mapper;

import com.oqplanner.tripranking.domain.TripRanking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripRankingMapper {

    int saveTripDayRanking(TripRanking tripDayRanking);
    List<TripRanking> getTripRanking();
//    List<TripRanking>  getTripWeekRanking();
//    List<TripRanking>  getTripMonthRanking();


}
