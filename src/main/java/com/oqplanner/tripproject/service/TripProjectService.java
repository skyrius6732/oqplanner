package com.oqplanner.tripproject.service;

import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripproject.mapper.TripProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TripProjectService {

    @Autowired
    private TripProjectMapper tripProjectMapper;

    public int saveProject(TripProject tripProject){
        return tripProjectMapper.saveProject(tripProject);
    }
    public int removeProject(Map paramMap){
        return tripProjectMapper.removeProject(paramMap);
    }






}
