package com.oqplanner.tripplan.service;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.mapper.TripPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TripPlanService {

    @Autowired
    private TripPlanMapper tripPlanMapper;

    public int savePlanInfo(TripPlan tripPlan) {
        return tripPlanMapper.savePlanInfo(tripPlan);
    }

    public List<TripPlan> getPlanInfoList(TripPlan tripPlan){

        return tripPlanMapper.getPlanInfoList(tripPlan);
    }

    public Map<String,String> getTripPlanStTimeLength(TripPlan tripPlan){
        return tripPlanMapper.getTripPlanStTimeLength(tripPlan);
    }

    public int removePlanInfo(TripPlan tripPlan){
        return tripPlanMapper.removePlanInfo(tripPlan);
    }
    public int removePlanInfoBytripPlanNo(String tripPlanNo){
        return tripPlanMapper.removePlanInfoBytripPlanNo(tripPlanNo);
    }

}
