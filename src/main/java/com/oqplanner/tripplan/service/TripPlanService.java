package com.oqplanner.tripplan.service;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.mapper.TripPlanMapper;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripproject.mapper.TripProjectMapper;
import com.oqplanner.tripschedule.mapper.TripScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TripPlanService {

    @Autowired
    private TripPlanMapper tripPlanMapper;

    @Autowired
    private TripProjectMapper tripProjectMapper;


    @Autowired
    private TripScheduleMapper tripScheduleMapper;

    public int savePlanInfo(TripPlan tripPlan) {

        // 여행 계획 생성
        int resultNum = tripPlanMapper.savePlanInfo(tripPlan);
        
        // 여행 프로젝트 생성
        // tripProject 객체의 tripProjNo UUID 값 생성
        String uuid = UUID.randomUUID().toString();
        String tripProjectNo = uuid.split("-")[uuid.split("-").length-1];

        // tripProjNo : UUID
        // tripUserNo : 사용자 ID 값 (추후에 세션에서 가져와야함)
        // tripPlanNo : 여행 계획 추가시 증가된 ID 값
        TripProject tripProject = TripProject.builder()
                .tripProjectNo(tripProjectNo)
                .tripUserNo("a70eadde8695")
                .tripPlanNo(tripPlan.getTripPlanNo())
                .build();

        tripProjectMapper.saveProject(tripProject);

        // 여행 일정 생성
        // 60m * 24h = 1440m
        // 일정 테이블 row 개수
        int rowNum = 1440 / tripPlan.getTripPlanTimeUnit();

        int tripDay = tripPlan.getTripPlanAllNum();
        int timeUnit = tripPlan.getTripPlanTimeUnit();

        // row 별 앞 시간
        String startTime = tripPlan.getTripPlanStTime();

        // row 별 뒤 시간
        String endTime = "";

        int timeSet = 0;
        HashMap paramMap = new HashMap<>();
        List<HashMap> scheduleList = new ArrayList<HashMap>();

        for(int i=1; i<=tripDay; i++){
            int order = 0;
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

                dayMap.put("tripScheduleOrder", order);
                dayMap.put("tripScheduleEdTime", endTime);
                dayMap.put("tripScheduleDay", i);
                dayMap.put("tripProjectNo", tripProjectNo);
                startTime = endTime;
                order = order++;
                scheduleList.add(dayMap);
            }
        }
        paramMap.put("scheduleList", scheduleList);
        tripScheduleMapper.saveSchedule(paramMap);

        
        return resultNum;
    }

    public List<TripPlan> getPlanInfoList(TripPlan tripPlan){

        return tripPlanMapper.getPlanInfoList(tripPlan);
    }

    public TripPlan getPlanInfo(TripPlan tripPlan){
        return tripPlanMapper.getPlanInfo(tripPlan);
    }

    public Map<String,String> getTripPlanStTimeLength(TripPlan tripPlan){
        return tripPlanMapper.getTripPlanStTimeLength(tripPlan);
    }

    public int removePlanInfo(TripPlan tripPlan){

        // 여행 프로젝트 데이터 삭제

        Map paramMap = new HashMap();
        paramMap.put("tripPlanNo",tripPlan.getTripPlanNo());
        tripProjectMapper.removeProject(paramMap);

        return tripPlanMapper.removePlanInfo(tripPlan);
    }
    public int removePlanInfoBytripPlanNo(String tripPlanNo){
        return tripPlanMapper.removePlanInfoBytripPlanNo(tripPlanNo);
    }

}
