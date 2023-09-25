package com.oqplanner.tripschedule.domain;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSchedule {
    private String tripScheduleNo;
    private String tripProjectNo;
    private String tripScheduleStDt;
    private String tripScheduleEdDt;
    private String tripScheduleStTime;
    private String tripScheduleEdTime;
    private String tripScheduleDepart;
    private String tripScheduleArrive;
    private String tripScheduleTransport;
    private int tripScheduleCost;
    private String tripScheduleNote;
    private int tripScheduleOrder;
    private int tripScheduleDay;
    private Date tripScheduleRedDt;
    private Date tripScheduleModDt;

}
