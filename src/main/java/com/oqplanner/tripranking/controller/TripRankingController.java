package com.oqplanner.tripranking.controller;

import com.oqplanner.tripranking.domain.TripRanking;
import com.oqplanner.tripranking.service.TripRankingService;
import com.oqplanner.tripschedule.domain.TripSchedule;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="trip-ranking-controller",  description = "여행 랭킹 Controller 입니다.")
@RestController
@RequestMapping("/trip/rank")
public class TripRankingController {

    private final TripRankingService tripRankingService;

    @Autowired
    public TripRankingController(TripRankingService tripRankingService){
        this.tripRankingService = tripRankingService;
    }

    @GetMapping("/info")
    public List<TripRanking> getTripRanking(){
        return tripRankingService.getTripRanking();
    }
//
//    @GetMapping("/info/week")
//    public List<TripRanking> getTripWeekRanking(){
//        return tripRankingService.getTripWeekRanking();
//    }
//
//    @GetMapping("/info/month")
//    public List<TripRanking> getTripMonthRanking(){
//        return tripRankingService.getTripMonthRanking();
//    }
}
