package com.oqplanner.tripproject.mapper;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripproject.domain.TripProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TripProjectMapper {
    int saveProject(TripProject tripProject);
    int removeProject(Map paramMap);
}
