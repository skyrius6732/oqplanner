package com.oqplanner.tripplan.mapper;


import com.oqplanner.tripplan.domain.TripPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TripPlanMapper {
    int savePlanInfo(TripPlan tripPlan);
    List<TripPlan> getPlanInfoList(TripPlan tripPlan);
    Map<String,String> getTripPlanStTimeLength(TripPlan tripPlan);
    int removePlanInfo(TripPlan tripPlan);

    int removePlanInfoBytripPlanNo(String tripPlanNo);

}
