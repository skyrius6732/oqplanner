package com.oqplanner.tripplan.controller;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.service.TripPlanService;
import com.oqplanner.tripuser.service.TripUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/trip/plan")
public class TripPlanController {

    private final TripPlanService tripPlanService;

    @Autowired
    public TripPlanController(TripUserService tripUserService, TripPlanService tripPlanService) {
        this.tripPlanService = tripPlanService;
    }

    @PostMapping("/info")
    public int savePlanInfo(@RequestBody TripPlan tripPlan) {
        return tripPlanService.savePlanInfo(tripPlan);
    }

    @GetMapping("/info")
    public List<TripPlan> getPlanInfo(@RequestBody TripPlan tripPlan) {
        return tripPlanService.getPlanInfoList(tripPlan);
    }

    @DeleteMapping("/info")
    public int removePlanInfo(@RequestBody TripPlan tripPlan){
        return tripPlanService.removePlanInfo(tripPlan);
    }
    @DeleteMapping("/info/{tripPlanNo}")
    public int removePlanInfoBytripPlanNo(@PathVariable String tripPlanNo){
        return tripPlanService.removePlanInfoBytripPlanNo(tripPlanNo);
    }



}
