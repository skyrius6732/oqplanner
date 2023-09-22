package com.oqplanner.tripschedule.service;

import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.service.TripPlanService;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripschedule.domain.TripSchedule;
import com.oqplanner.tripschedule.mapper.TripScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripScheduleService {

    @Autowired
    private TripScheduleMapper tripScheduleMapper;

    private TripPlanService tripPlanService;

    public int saveSchedule(TripProject tripProject, String type){

        // [여행 일정 생성]
        // 60m * 24h = 1440m
        // 일정 테이블 row 개수

        TripPlan tripPlan = tripProject.getTripPlan();
        String tripProjectNo = tripProject.getTripProjectNo();

        int rowNum = 1440 / tripPlan.getTripPlanTimeUnit();
        int timeUnit = tripPlan.getTripPlanTimeUnit();

        int tripDay = 1;    // add Default (일정 추가시 사용)
        if(type.equals("new")) {   
            tripDay = tripPlan.getTripPlanAllNum();
        }
        // row 별 앞 시간
        String startTime = tripPlan.getTripPlanStTime();

        // row 별 뒤 시간
        String endTime = "";

        int timeSet = 0;
        HashMap paramMap = new HashMap<>();
        List<HashMap> scheduleList = new ArrayList<HashMap>();

        for(int i=1; i<=tripDay; i++){
            for(int j=0; j<rowNum; j++) {
                HashMap dayMap = new HashMap();
                timeSet += timeUnit;
                timeSet = timeSet % 60;
                String frontSt = startTime.substring(0, 2);

                dayMap.put("tripScheduleStTime", startTime);

                if (timeSet != 0) {
                    frontSt = frontSt + timeSet;
                    endTime = frontSt;
                } else {
                    int intNum = Integer.parseInt(frontSt);
                    if(timeUnit <= 60) {
                        intNum = intNum + 1;
                    }else if(60 < timeUnit && timeUnit <= 120){
                        intNum = intNum + 2;
                    }else if(120 < timeUnit && timeUnit <= 180){
                        intNum = intNum + 3;
                    }else if(180 < timeUnit && timeUnit <= 240){
                        intNum = intNum + 4;
                    }

                    if (intNum < 10) {
                        frontSt = "0" + intNum;
                        endTime = frontSt + "00";
                    }else{
                        endTime = intNum + "00";
                    }

                    timeSet = 0;
                }

                int intNum = Integer.parseInt(endTime);
                if(intNum >= 2400){
                    if(intNum == 2400) {
                        endTime = "0000";
                    }else{
                        intNum = intNum - 2400;
                        endTime = "0" + intNum;
                    }
                }


                dayMap.put("tripScheduleEdTime", endTime);
                dayMap.put("tripProjectNo", tripProjectNo);
                dayMap.put("tripScheduleOrder", j);
                if(type.equals("new")){
                    dayMap.put("tripScheduleDay", i);
                }else{
                    // tripScheduleDay 기존값 +1 값을 넣어야함
                    // mapper 사용
                    int maxScheduleDay = tripScheduleMapper.getMaxTripScheduleDay(tripProjectNo);
                    dayMap.put("tripScheduleDay", maxScheduleDay+1);
                }

                startTime = endTime;
                scheduleList.add(dayMap);
            }
        }
        paramMap.put("scheduleList", scheduleList);

        return tripScheduleMapper.saveSchedule(paramMap);

    }

    public List<TripSchedule> getScheduleList (TripSchedule tripSchedule){
        return tripScheduleMapper.getScheduleList (tripSchedule);
    }

    public int addSchedule(TripProject tripProject){
        // tripProjectNo로 tripPlan 정보 찾아
        // saveSchedule의 파라미터로 전달

        // 추후에 앞단에서 session 통해 tripProjectNo 가져 와야함
        // 현재는 포스트맨에서 전달

        tripProject = tripScheduleMapper.getPlanInfoBytripProjectNo(tripProject);

        return saveSchedule(tripProject,"add");
    }

    public int modifySchedule(TripSchedule tripSchedule){

        int scheduleDay = tripSchedule.getTripScheduleDay();

        if(scheduleDay>=0){
            int today = tripSchedule.getTripScheduleDay();
            // 수정값 날짜 이전 날짜의 마지막 행 TRIP_SCHEDULE_ED_TIME
            tripSchedule.setTripScheduleDay(today-1);
            String beforeDayEdTime = tripScheduleMapper.getBeforeDayEdTime(tripSchedule);
            // 수정값 날짜 이후 날짜의 마지막 행 TRIP_SCHEDULE_ST_TIME
            tripSchedule.setTripScheduleDay(today+1);
            String afterDayStTime = tripScheduleMapper.getAfterDayStTime(tripSchedule);

            boolean checkFlag = false;

            if(beforeDayEdTime != null || beforeDayEdTime.equals("")){
                if(!tripSchedule.getTripScheduleStTime().equals(beforeDayEdTime)){
                    checkFlag = false;
                }else{
                    checkFlag = true;

                }
            }

            if(afterDayStTime != null || afterDayStTime.equals("")){
                if(!tripSchedule.getTripScheduleEdTime().equals(afterDayStTime)){
                    checkFlag = false;
                }else{
                    checkFlag = true;
                }
            }
            if(checkFlag){
                return tripScheduleMapper.modifySchedule(tripSchedule);
            }else{
                return -1;
            }

        }
        // -1 이전 값이나 이후 값이 현재 날짜와 같지 않음
        return -1;
    }


}
