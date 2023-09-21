package com.oqplanner.tripschedule.service;

import com.oqplanner.tripschedule.mapper.TripScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TripScheduleService {

    @Autowired
    private TripScheduleMapper tripScheduleMapper;

    public int saveSchedule(HashMap paramMap){
        return tripScheduleMapper.saveSchedule(paramMap);
    }
}
