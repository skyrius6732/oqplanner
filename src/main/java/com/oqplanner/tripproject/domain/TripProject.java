package com.oqplanner.tripproject.domain;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripschedule.domain.TripSchedule;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripProject {

    private String tripProjectNo;
    private TripPlan tripPlan;
//    private String tripPlanNo;
    private String tripUserNo;
    private List<TripSchedule> tripScheduleList;

}
