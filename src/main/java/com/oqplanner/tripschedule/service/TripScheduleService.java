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

    public int saveSchedule(TripProject tripProject, String type){

        // [여행 일정 생성]
        // 60m * 24h = 1440m
        // 일정 테이블 row 개수

        TripPlan tripPlan = tripProject.getTripPlan();
        String tripProjectNo = tripProject.getTripProjectNo();

        int rowNum = 1440 / tripPlan.getTripPlanTimeUnit();
        int timeUnit = tripPlan.getTripPlanTimeUnit();

        // yyyymmdd를 넣기위한 캘린더 생성
        Date date = null;
        if(type.equals("new")) {    // new : 신규건
            date = tripPlan.getTripPlanStDt();
        }else{  // add : 추가건
            date =  tripPlan.getTripPlanEdDt(); // 마지막날 planEdDt를 가져와야함
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int nightIdx = 0;

        int tripDay = 0;
        if(type.equals("new")) {   // new : 신규건
            tripDay = tripPlan.getTripPlanAllNum();
        }else{  // add : 추가건
            tripDay = 1; // add Default (일정 추가시 1일치 schedule만 생성)
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
                        intNum = intNum + 1;        // 1시간
                    }else if(60 < timeUnit && timeUnit <= 120){
                        intNum = intNum + 2;        // 2시간
                    }else if(120 < timeUnit && timeUnit <= 180){
                        intNum = intNum + 3;        // 3시간
                    }else if(180 < timeUnit && timeUnit <= 240){
                        intNum = intNum + 4;        // 4시간
                    }else if(240 < timeUnit && timeUnit <= 360){
                        intNum = intNum + 6;        // 6시간
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

        // 추후에 saveSchedule 메서드 태우기전에
        // 예를들어 여행일자가 4일 일때 추가적으로 5일째 schedule 등록방지를 위해
        // 체크로직이 필요함(해당 사항은 백단보다 프론트단에서 4일이 등록이 되어있다면
        // UI 적으로 + 버튼을 없애 진행하는것이 좋겠음)
        // 4일로 정했으면 추가적으로 안넣겠다는거구나..? (2023-10-18) 알겟슴
        tripProject = tripScheduleMapper.getPlanInfoBytripProjectNo(tripProject);

        return saveSchedule(tripProject,"add");
    }


    public int checkModifySchedule(TripSchedule tripSchedule) throws ParseException {

        int result = 0;
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

        if(tripScheduleDay == 1 && tripScheduleOrder == 0){
                int paramDay = tripScheduleDay;
                int paramOrder = tripScheduleOrder+1;

                TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();
                Date orderStDt = dateFormat.parse(tripScheduleMapper.getOrderStDt(param));
                int currentEdToOrderSt = currentEdDt.compareTo(orderStDt);
                if(currentEdToOrderSt > 0){
                    result = -1;
                }
        }else if(tripScheduleDay == maxTripScheduleDay && tripScheduleOrder == maxTripScheduleOrder){
                int paramDay = tripScheduleDay;
                int paramOrder = tripScheduleOrder-1;

                TripSchedule param = TripSchedule.builder().tripProjectNo(tripProjectNo)
                        .tripScheduleDay(paramDay)
                        .tripScheduleOrder(paramOrder)
                        .build();

                Date orderEdDt = dateFormat.parse(tripScheduleMapper.getOrderEdDt(param));
                int orderEdTocurrentSt = orderEdDt.compareTo(currentStDt);

                if(orderEdTocurrentSt > 0) {
                    result = -1;
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
                    result = -1;
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
                    result = -1;
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
                    result = -1;
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
                    result = -1;
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
                    result = -1;
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
                    result = -1;
                }
            }
        }

        return result;
    }


    public int modifySchedule(TripSchedule tripSchedule) throws ParseException {

        int result = this.checkModifySchedule(tripSchedule);
        if(result == 0){
            return tripScheduleMapper.modifySchedule(tripSchedule);
        }
        return result;
    }

    public int modifyScheduleList(TripProject tripProject) throws ParseException{

        int idx = 0;
        List<TripSchedule> tripScheduleList = tripProject.getTripScheduleList();
        for(TripSchedule tripSchedule : tripScheduleList ) {
            int result = this.checkModifySchedule(tripSchedule);
            if(result == 0){
                tripScheduleMapper.modifySchedule(tripSchedule);
                idx++;
            }
        }
        return idx;
    }

    public Map getScheduleListByDate(TripProject tripProject){
        Map returnMap = new HashMap();

        tripProject = tripScheduleMapper.getPlanInfoBytripProjectNo(tripProject);
        String tripProjectNo = tripProject.getTripProjectNo();
        int planNum = tripProject.getTripPlan().getTripPlanAllNum();
        returnMap.put("TRIP_PLAN_ST_DT", tripProject.getTripPlan().getTripPlanStDt());
        returnMap.put("TRIP_PLAN_ED_DT", tripProject.getTripPlan().getTripPlanEdDt());
        returnMap.put("TRIP_PLAN_ALL_NUM", planNum);

        // String to Date 후 형식변환 하여 select 태움
        Date date = tripProject.getTripPlan().getTripPlanStDt();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        List<Map> scheduleList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=1; i<=planNum; i++) {
            TripSchedule tripSchedule = TripSchedule.builder()
                    .tripProjectNo(tripProjectNo)
                    .tripScheduleDay(i).build();
            Map map = tripScheduleMapper.getScheduleListByDate(tripSchedule);

            if(map == null){
                map = new HashMap();
                map.put("deletedDate",true);
            }else{
                String currentDay = sdf.format(calendar.getTime());
                map.put("TRIP_CURRENT_DAY", currentDay);
                calendar.add(Calendar.DATE, 1); // 일 증가
                map.put("deletedDate",false);
            }
            scheduleList.add(map);
        }
        returnMap.put("scheduleList", scheduleList);


        return returnMap;
    }

    public int removeSchedule(Map<String,Object> paramMap) throws ParseException {

        int returnNum = 0;
        String tripProjectNo = (String)paramMap.get("tripProjectNo");
        int tripDelDay = (Integer) paramMap.get("tripScheduleDay");



        TripProject tripProject = TripProject.builder().tripProjectNo(tripProjectNo).build();
        tripProject = tripScheduleMapper.getPlanInfoBytripProjectNo(tripProject);
        int planNum = tripProject.getTripPlan().getTripPlanAllNum();


        // 삭제 파라미터(projectNo과 삭제될 tripScheduleDay로 삭제 진행)
        TripSchedule tripSchedule = TripSchedule.builder()
                .tripProjectNo(tripProjectNo)
                .tripScheduleDay(tripDelDay).build();
        
        // 삭제 파라미터(projectNo과 삭제될 tripScheduleDay로 
        // 이전 날짜의 마지막 스케쥴의 끝 시간을 tripScheduleEdTime 구하고
        // 이후 날짜의 첫시간을 구해준 tripScheduleEdTime값을 대입 해줌)
        String tripScheduleEdTime = tripScheduleMapper.getBeforeDayEdTime(tripSchedule);
        
        returnNum = tripScheduleMapper.removeSchedule(tripSchedule);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        
        // 삭제된 Day 이후에 +1일 부터
        // TRIP_SCHEDULE_DAY -1
        // TRIP_SCHEDULE_ST_DT / TRIP_SCHEDULE_ED_DT -1일 씩 진행
        // (해당 날짜형식으로 String to Calendar로 진행할것)
        for(int i=tripDelDay+1; i<=planNum; i++){
            tripSchedule = TripSchedule.builder()
                    .tripProjectNo(tripProjectNo)
                    .tripScheduleDay(i).build();

            // max order 값 조회
            // int maxOrder = tripScheduleMapper.getMaxTripScheduleOrder(tripSchedule);
            // 0~max order 까지 순차 수정
            /*
                for(int j=0; i<maxOrder; j++){
                tripSchedule.setTripScheduleOrder(j);
            }
            */
            
            List<TripSchedule> scheduleList = tripScheduleMapper.getScheduleList(tripSchedule);
            int scheduleListSize = scheduleList.size();

            for(int k=0; k<scheduleListSize; k++){
                tripSchedule = scheduleList.get(k);

                if(k==0){       // 처음 스케쥴의 시작 시간을
                    // 이전 일자의 마지막 스케쥴의 끝 시간과 같은 시간 삽입
                    tripSchedule.setTripScheduleStTime(tripScheduleEdTime);
                }
                
                // 현재 i = tripDelDay + 1 이므로
                tripSchedule.setTripScheduleDay(i);

                // String to Date 변환
                Date tripScheduleStDt = sdf.parse(tripSchedule.getTripScheduleStDt());
                Date tripScheduleEdDt = sdf.parse(tripSchedule.getTripScheduleEdDt());

                // Calendar 변환 후 -1 진행 하고 Date to String 변환
                Calendar stCalendar = Calendar.getInstance();
                stCalendar.setTime(tripScheduleStDt);
                stCalendar.add(Calendar.DATE,-1);
                String strStDt = sdf.format(stCalendar.getTime());


                Calendar edCalendar = Calendar.getInstance();
                edCalendar.setTime(tripScheduleEdDt);
                edCalendar.add(Calendar.DATE,-1);
                String strEdDt = sdf.format(edCalendar.getTime());
                
                // 변경 날짜 셋팅
                tripSchedule.setTripScheduleStDt(strStDt);
                tripSchedule.setTripScheduleEdDt(strEdDt);
                
                // 해당 tripScheduleDay의 tripScheduleOrder번째 where 절로 넣기위한 셋팅
                tripSchedule.setTripScheduleOrder(k);

                tripScheduleMapper.modifySchedule(tripSchedule);
            }
        }



        return returnNum;
    }




}
