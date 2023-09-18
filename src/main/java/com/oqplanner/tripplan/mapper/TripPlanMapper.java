package com.oqplanner.tripplan.mapper;


import com.oqplanner.tripplan.domain.TripPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TripPlanMapper {
    public int savePlanInfo(TripPlan tripPlan);
    public List<TripPlan> getPlanInfoList(TripPlan tripPlan);
    public Map<String,String> getTripPlanStTimeLength(TripPlan tripPlan);
    public int removePlanInfo(TripPlan tripPlan);

    public int removePlanInfoBytripPlanNo(String tripPlanNo);

}
