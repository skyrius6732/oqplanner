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
    List<TripCost> getPrivateCostList (TripCost.TripCostRequest request);

    int modifyPublicCost(TripCost tripCost);
    int modifyPrivateCost(TripCost tripCost);

    int removePublicCost(TripCost tripCost);
    int removePrivateCost(TripCost tripCost);
}
