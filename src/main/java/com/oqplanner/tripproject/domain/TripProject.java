package com.oqplanner.tripproject.domain;


import com.oqplanner.tripplan.domain.TripPlan;
import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripProject {

    private String tripProjectNo;
    private int tripPlanNo;
    private String tripUserNo;

}
