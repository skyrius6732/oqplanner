package com.oqplanner.tripcompanion.domain;

import com.oqplanner.tripcost.domain.TripCost;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCompanion {
    private String tripCompanionNo;
    private String tripProjectNo;
    private String tripCompanionNm;
    private String tripCompanionOrder;
    private String tripCompanionRegDt;
    private String tripCompanionModDt;

    private List<TripCost.TripPrivateCost> tripPrivateCostList;

}
