package com.oqplanner.tripcost.service;


import com.oqplanner.tripcompanion.domain.TripCompanion;
import com.oqplanner.tripcompanion.mapper.TripCompanionMapper;
import com.oqplanner.tripcost.domain.TripCost;
import com.oqplanner.tripcost.mapper.TripCostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCostService {

    @Autowired
    private TripCostMapper tripCostMapper;

    @Autowired
    private TripCompanionMapper tripCompanionMapper;

    public int savePrivateCost(List<TripCost> tripCostList){
        int listSize = tripCostList.size();

        try{
            for(TripCost tripCost : tripCostList){
                tripCostMapper.savePrivateCost(tripCost);
                tripCostMapper.saveCost(tripCost);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listSize;
    }
    public int savePublicCost(List<TripCost> tripCostList){

        int listSize = tripCostList.size();
        try{
            for(TripCost tripCost : tripCostList){
                tripCostMapper.savePublicCost(tripCost);
                tripCostMapper.saveCost(tripCost);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listSize;


    }
    public int saveCost(TripCost tripCost){
        return tripCostMapper.saveCost(tripCost);
    }

    public List<TripCost> getPublicCostList(TripCost tripCost){
        return tripCostMapper.getPublicCostList(tripCost);
    }

    public List<TripCost> getPrivateCostList(TripCost.TripCostRequest request){
        return tripCostMapper.getPrivateCostList(request);
    }

    public int modifyPublicCost(List<TripCost> tripCostList){

        int listSize = tripCostList.size();

        for(TripCost tripCost : tripCostList){
            tripCostMapper.modifyPublicCost(tripCost);
        }

        return listSize;
    }

    public int modifyPrivateCost(List<TripCost> tripCostList){
        int listSize = tripCostList.size();

        for(TripCost tripCost : tripCostList){
            tripCostMapper.modifyPrivateCost(tripCost);
        }
        return listSize;
    }

    public int removePublicCost(List<TripCost.TripCostRequest> tripCostList){
        int listSize = tripCostList.size();
        int successNum = 0;

        try{
            for(TripCost.TripCostRequest request : tripCostList){
                int num = tripCostMapper.removePublicCost(request);
                if(num == 1) {
                    tripCostMapper.removeCost(request);
                    successNum += num;
                }else{
                    return -1;
                }
            }
            if(listSize != successNum){
                return -1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return successNum;
    }

    public int removePrivateCost(List<TripCost.TripCostRequest> tripCostList){
        int listSize = tripCostList.size();
        int successNum = 0;

        try{
            for(TripCost.TripCostRequest request : tripCostList){
                int num = tripCostMapper.removePrivateCost(request);
                if(num == 1) {
                    tripCostMapper.removeCost(request);
                    successNum += num;
                }else{
                    return -1;
                }
            }
            if(listSize != successNum){
                return -1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return successNum;
    }

    public void removeCostAll(TripCost.TripCostRequest request){
        try{
            TripCompanion tripCompanion = TripCompanion.builder()
                    .tripProjectNo(request.getTripProjectNo()).build();
            tripCostMapper.removePublicCost (request);
            tripCostMapper.removePrivateCost (request);
            tripCompanionMapper.removeCompanion(tripCompanion);
            tripCostMapper.removeCost(request);
        }catch (Exception e){
            e.printStackTrace();
        }

    }









}
