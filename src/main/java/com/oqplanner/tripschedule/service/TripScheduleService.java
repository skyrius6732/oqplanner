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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        // yyyymmdd를 넣기위한 캘린더 생성
        Date date = tripPlan.getTripPlanStDt();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int nightIdx = 0;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=1; i<=tripDay; i++){
            for(int j=0; j<rowNum; j++) {
                HashMap dayMap = new HashMap();
                timeSet += timeUnit;
                timeSet = timeSet % 60;
                String frontSt = startTime.substring(0, 2);




                String strDate = sdf.format(calendar.getTime());
                strDate = strDate.replace("-","");

                dayMap.put("tripScheduleStDt", strDate);
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

                    nightIdx = 1;
                    calendar.add(Calendar.DATE, nightIdx);
                    strDate = sdf.format(calendar.getTime());
                    strDate = strDate.replace("-","");
                    if(intNum == 2400) {
                        endTime = "0000";
                    }else{
                        intNum = intNum - 2400;
                        endTime = "0" + intNum;
                    }

                }
//                dayMap.put("tripPlanYyyymmdd", strDate);

                dayMap.put("tripScheduleEdDt", strDate);
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


    public int modifySchedule(TripSchedule tripSchedule) throws ParseException {
//        String bdet = tripScheduleMapper.getBeforeDayEdTime(tripSchedule);
//        String adst = tripScheduleMapper.getAfterDayStTime(tripSchedule);
//        String boet = tripScheduleMapper.getBeforeOrderEdTime(tripSchedule);
//        String aost = tripScheduleMapper.getAfterOrderStTime(tripSchedule);

        String tripProjectNo = tripSchedule.getTripProjectNo();

        TripProject tripProject = TripProject.builder()
                .tripProjectNo(tripProjectNo)
                .build();

        tripProject = tripScheduleMapper.getPlanInfoBytripProjectNo(tripProject);


        int tripScheduleDay = tripSchedule.getTripScheduleDay();
        int tripScheduleOrder = tripSchedule.getTripScheduleOrder();
        int maxTripScheduleDay = tripProject.getTripPlan().getTripPlanAllNum();    // TripScheduleDay 최대값
        int maxTripScheduleOrder = tripScheduleMapper.getMaxTripScheduleOrder(tripSchedule);    // TripScheduleOrder 최대값



        // 포스트맨 말고 해당 row값의 stdt, eddt, orderStDt, orderEdDt, order를 전달해서 진행해야함
        String tripScheduleStDt = tripSchedule.getTripScheduleStDt();
        String tripScheduleEdDt = tripSchedule.getTripScheduleEdDt();

        String currentScheduleStTime = tripSchedule.getTripScheduleStTime();
        String currentScheduleEdTime = tripSchedule.getTripScheduleEdTime();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");


        Date currentStDt = dateFormat.parse(tripScheduleStDt+currentScheduleStTime);
        Date currentEdDt = dateFormat.parse(tripScheduleEdDt+currentScheduleEdTime);

        if(tripScheduleDay == 1){
            if(tripScheduleOrder == 0){
                int paramDay = tripScheduleDay;
                int paramOrder = tripScheduleOrder+1;

                TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();
                Date orderStDt = dateFormat.parse(tripScheduleMapper.getOrderStDt(param));
                int currentEdToOrderSt = currentEdDt.compareTo(orderStDt);
                if(currentEdToOrderSt > 0){
                    return -1;
                }
            }
        }else if(tripScheduleDay == maxTripScheduleDay){
            if(tripScheduleOrder == maxTripScheduleOrder){
                int paramDay = tripScheduleDay;
                int paramOrder = tripScheduleOrder-1;

                TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderEdDt = dateFormat.parse(tripScheduleMapper.getOrderEdDt(param));
                int orderEdTocurrentSt = orderEdDt.compareTo(currentStDt);

                if(orderEdTocurrentSt > 0){
                    return -1;
                }
            }
        }else{


            if(tripScheduleOrder == 0){
                 int paramDay = tripScheduleDay-1;
                 int paramOrder = maxTripScheduleOrder;

                 TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderEdDt = dateFormat.parse(tripScheduleMapper.getOrderEdDt(param));
                int orderEdTocurrentSt = orderEdDt.compareTo(currentStDt);
                if(orderEdTocurrentSt > 0){
                    return -1;
                }

                paramDay = tripScheduleDay;
                paramOrder = tripScheduleOrder+1;

                param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderStDt = dateFormat.parse(tripScheduleMapper.getOrderStDt(param));
                int currentEdToOrderSt = currentEdDt.compareTo(orderStDt);
                if(currentEdToOrderSt > 0){
                    return -1;
                }


            }else if(tripScheduleOrder == maxTripScheduleOrder){
                 int paramDay = tripScheduleDay+1;
                 int paramOrder = 0;

                 TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderStDt = dateFormat.parse(tripScheduleMapper.getOrderStDt(param));
                int currentEdToOrderSt = currentEdDt.compareTo(orderStDt);
                if(currentEdToOrderSt > 0){
                    return -1;
                }

                paramDay = tripScheduleDay;
                paramOrder = tripScheduleOrder-1;

                param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderEdDt = dateFormat.parse(tripScheduleMapper.getOrderEdDt(param));
                int orderEdTocurrentSt = orderEdDt.compareTo(currentStDt);
                if(orderEdTocurrentSt > 0){
                    return -1;
                }


            }else{
                 int paramDay = tripScheduleDay;
                 int paramOrder = tripScheduleOrder-1;
                 TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderEdDt = dateFormat.parse(tripScheduleMapper.getOrderEdDt(param));
                int orderEdTocurrentSt = orderEdDt.compareTo(currentStDt);
                if(orderEdTocurrentSt > 0){
                    return -1;
                }


                // 이후값 비교
                paramDay = tripScheduleDay;
                paramOrder = tripScheduleOrder+1;

                param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderStDt = dateFormat.parse(tripScheduleMapper.getOrderStDt(param));
                int currentEdToOrderSt = currentEdDt.compareTo(orderStDt);
                if(currentEdToOrderSt > 0){
                    return -1;
                }
            }
        }

        return tripScheduleMapper.modifySchedule(tripSchedule);
    }


}
