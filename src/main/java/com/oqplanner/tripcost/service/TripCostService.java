package com.oqplanner.tripcost.service;

import com.oqplanner.tripcost.domain.TripCost;
import com.oqplanner.tripcost.mapper.TripCostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCostService {

    @Autowired
    private TripCostMapper tripCostMapper;

    public int savePrivateCost(List<TripCost> tripCostList){
        int listSize = tripCostList.size();

        for(TripCost tripCost : tripCostList){
            tripCostMapper.savePrivateCost(tripCost);
            tripCostMapper.saveCost(tripCost);
        }
        return listSize;

    }

    public int savePublicCost(List<TripCost> tripCostList){

        int listSize = tripCostList.size();

        for(TripCost tripCost : tripCostList){
            tripCostMapper.savePublicCost(tripCost);
            tripCostMapper.saveCost(tripCost);
        }
        return listSize;
    }

    public int saveCost(TripCost tripCost){
        return tripCostMapper.saveCost(tripCost);
    }

    public List<TripCost> getPublicCostList(TripCost tripCost){
        return tripCostMapper.getPublicCostList(tripCost);
    }

    public List<TripCost> getPrivateCostList(TripCost tripCost){
        return tripCostMapper.getPrivateCostList(tripCost);
    }

    public int modifyPublicCost(List<TripCost> tripCostList){
        return tripCostMapper.modifyPublicCost(tripCostList);
    }

    public int modifyPrivateCost(List<TripCost> tripCostList){
        return tripCostMapper.modifyPrivateCost(tripCostList);
    }

    public int removePublicCost(TripCost tripCost){
        return tripCostMapper.removePublicCost(tripCost);
    }

    public int removePrivateCost(TripCost tripCost){
        return tripCostMapper.removePrivateCost(tripCost);
    }








}
