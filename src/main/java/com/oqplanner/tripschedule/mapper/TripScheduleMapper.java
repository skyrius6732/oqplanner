package com.oqplanner.tripschedule.mapper;

import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripschedule.domain.TripSchedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface TripScheduleMapper {
    int saveSchedule(HashMap param);
    List<TripSchedule> getScheduleList (TripSchedule tripSchedule);

    TripProject getPlanInfoBytripProjectNo(TripProject tripProject);

    int getMaxTripScheduleDay(String tripProjectNo);

    int modifySchedule(TripSchedule tripSchedule);
    String getBeforeDayEdTime(TripSchedule tripSchedule);
    String getAfterDayStTime(TripSchedule tripSchedule);
}
