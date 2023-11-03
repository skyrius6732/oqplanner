package com.oqplanner.tripproject.mapper;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripproject.domain.TripProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TripProjectMapper {

    TripProject getProject(TripPlan tripPlan);
    int saveProject(TripProject tripProject);
    int removeProject(Map paramMap);

}
