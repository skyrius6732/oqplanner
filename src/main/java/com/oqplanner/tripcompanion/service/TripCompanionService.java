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

    public int saveCompanion(TripCompanion tripCompanion){

//        List<TripCompanion> list = null;
//        // 동행자가 있는지 체크
//        for(TripCompanion tripCompanion :  tripCompanionList){
//            list = tripCompanionMapper.getCompanionList(tripCompanion);
//        }
//
//        if(list.size() > 0){
//            return -1;
//        }
        tripCompanionMapper.saveCompanion(tripCompanion);
        int companionNo = Integer.parseInt(tripCompanion.getTripCompanionNo());
        System.out.println("companionNo :: " + companionNo);

        return companionNo;
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
