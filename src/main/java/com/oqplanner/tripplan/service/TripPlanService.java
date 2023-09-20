package com.oqplanner.tripplan.service;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.mapper.TripPlanMapper;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripproject.mapper.TripProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripPlanService {

    @Autowired
    private TripPlanMapper tripPlanMapper;

    @Autowired
    private TripProjectMapper tripProjectMapper;
    

    public int savePlanInfo(TripPlan tripPlan) {


        int resultNum = tripPlanMapper.savePlanInfo(tripPlan);


        // tripProject 객체의 tripProjNo UUID 값 생성
        String uuid = UUID.randomUUID().toString();
        String tripPlanNo = uuid.split("-")[uuid.split("-").length-1];


        // tripProjNo : UUID
        // tripUserNo : 사용자 ID 값 (추후에 세션에서 가져와야함)
        // tripPlanNo : 여행 계획 추가시 증가된 ID 값
        TripProject tripProject = TripProject.builder()
                .tripProjectNo(tripPlanNo)
                .tripUserNo("a70eadde8695")
                .tripPlanNo(tripPlan.getTripPlanNo())
                .build();

        tripProjectMapper.saveProject(tripProject);

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
