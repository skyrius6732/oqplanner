package com.oqplanner.tripcost.domain;

import com.oqplanner.tripcompanion.domain.TripCompanion;
import lombok.*;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCost {

    private int tripCostNo;
    private String tripProjectNo;

    private String tripPrivateCostNo;
    private String tripPublicCostNo;

    private TripPrivateCost tripPrivateCost;
    private TripPublicCost tripPublicCost;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripPrivateCost {
        private String tripPrivateCostNo;
        private String tripCompanionNo;
        private String tripPrivateCostUse;
        private String tripPrivateCostOrder;
        private int tripPrivateCost;
        private String tripPrivateCostNote;
        private String tripPrivateCostRegDt;
        private String tripPrivateCostModDt;
//        private List<Cost> costList;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripPublicCost {
        private String tripPublicCostNo;
        private String tripPublicCostUse;
        private int tripPublicCostOrder;
        private int tripPublicCost;
        private String tripPublicCostNote;
        private String tripPublicCostRegDt;
        private String tripPublicCostModDt;
//        private List<Cost> costList;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripCostRequest{
        private String tripProjectNo;
        private String tripCompanionNo;
        private String tripPublicCostNo;
        private String tripPrivateCostNo;
    }
}
