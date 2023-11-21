package com.oqplanner.tripranking.service;

import com.oqplanner.tripranking.domain.TripRanking;
import com.oqplanner.tripranking.mapper.TripRankingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripRankingService {

    @Autowired
    private TripRankingMapper tripRankingMapper;

    public List<TripRanking> getTripRanking(){
        return tripRankingMapper.getTripRanking();
    }

//    public List<TripRanking> getTripWeekRanking(){
//        return tripRankingMapper.getTripWeekRanking();
//    }
//
//    public List<TripRanking> getTripMonthRanking(){
//        return tripRankingMapper.getTripMonthRanking();
//    }


}
