package com.oqplanner.tripcost.mapper;

import com.oqplanner.tripcost.domain.TripCost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripCostMapper {

    int savePrivateCost(TripCost tripCost);
    int savePublicCost(TripCost tripCost);
    int saveCost(TripCost tripCost);

    List<TripCost> getPublicCostList(TripCost tripCost);
    List<TripCost> getPrivateCostList (TripCost tripCost);

    int modifyPublicCost(List<TripCost> tripCostList);
    int modifyPrivateCost(List<TripCost> tripCostList);

    int removePublicCost(TripCost tripCost);
    int removePrivateCost(TripCost tripCost);
}
