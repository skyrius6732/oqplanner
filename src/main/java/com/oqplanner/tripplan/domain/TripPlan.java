package com.oqplanner.tripplan.domain;

import lombok.*;
import org.apache.ibatis.annotations.Param;

import java.beans.ConstructorProperties;
import java.util.Date;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripPlan {
    private int tripPlanNo;
    private String tripPlanNm;
    private String tripPlannerNm;
    private Date tripPlanStDt;
    private Date tripPlanEdDt;
    private String tripPlanDefaultYn;
    private String tripPlanStTime;
    private String tripPlanTimeUnit;
    private String tripPlanAllNum;
    private Date tripPlanRegDt;
    private Date tripPlanModDt;

}
