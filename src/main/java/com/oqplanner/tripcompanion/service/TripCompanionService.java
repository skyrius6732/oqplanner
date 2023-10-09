package com.oqplanner.tripcompanion.service;


import com.oqplanner.tripcompanion.domain.TripCompanion;
import com.oqplanner.tripcompanion.mapper.TripCompanionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCompanionService {
    @Autowired
    private TripCompanionMapper tripCompanionMapper;

    public int saveCompanion(List<TripCompanion> tripCompanionList){
        return tripCompanionMapper.saveCompanion(tripCompanionList);
    }
    public List<TripCompanion> getCompanionList(TripCompanion tripCompanion){
        return tripCompanionMapper.getCompanionList(tripCompanion);
    }
    public int modifyCompanionList(List<TripCompanion> tripCompanionList){
        return tripCompanionMapper.modifyCompanionList(tripCompanionList);
    }
    public int removeCompanion(TripCompanion tripCompanion){
        return tripCompanionMapper.removeCompanion(tripCompanion);
    }

}