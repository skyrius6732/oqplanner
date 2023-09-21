package com.oqplanner.tripschedule.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface TripScheduleMapper {
    int saveSchedule(HashMap param);
}
