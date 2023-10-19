package com.oqplanner.tripplan.service;


import com.oqplanner.common.SessionConst;
import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.mapper.TripPlanMapper;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripproject.mapper.TripProjectMapper;
import com.oqplanner.tripschedule.mapper.TripScheduleMapper;
import com.oqplanner.tripschedule.service.TripScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private TripScheduleService tripScheduleService;

    @Autowired
    private final HttpServletRequest request; // HttpServletRequest 주입




    public int savePlanInfo(TripPlan tripPlan) {

        // [여행 계획 생성]
        int resultNum = tripPlanMapper.savePlanInfo(tripPlan);

        // [여행 프로젝트 생성]
        // tripProject 객체의 tripProjNo UUID 값 생성
        String uuid = UUID.randomUUID().toString();
        String tripProjectNo = uuid.split("-")[uuid.split("-").length-1];


        HttpSession session = request.getSession();
        // 여행 프로젝트 tripProjectNo 값 세션에 넣기
        session.setAttribute(SessionConst.TRIP_PROJECT_NO, tripProjectNo);

//        String tripUserNo = "aaabbbcdd";
//        if(session.getAttribute(SessionConst.TRIP_USER_NO) != null){
//            tripUserNo = (String)session.getAttribute(SessionConst.TRIP_USER_NO);
//        }

        // tripProjNo : UUID
        // tripUserNo : 사용자 ID 값 (추후에 세션에서 가져와야함)
        // tripPlanNo : 여행 계획 추가시 증가된 ID 값
        TripProject tripProject = TripProject.builder()
                .tripProjectNo(tripProjectNo)
                .tripUserNo(session.getAttribute(SessionConst.TRIP_USER_NO).toString()) // 여행 유저 tripUserNo 값 세션에서 가져오기
                .tripPlan(tripPlan)
                .build();

        // 여행 프로젝트 생성
        tripProjectMapper.saveProject(tripProject);

        // 여행 일정 생성
        tripScheduleService.saveSchedule(tripProject,"new");

        
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
