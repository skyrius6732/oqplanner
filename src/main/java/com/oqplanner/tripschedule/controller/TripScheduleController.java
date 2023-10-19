package com.oqplanner.tripschedule.controller;



import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripschedule.domain.TripSchedule;
import com.oqplanner.tripschedule.service.TripScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Tag(name="trip-schedule-controller",  description = "여행 일정 Controller 입니다.")
@RestController
@RequestMapping("/trip/schedule")
public class TripScheduleController {

    private final TripScheduleService tripScheduleService;

    @Autowired
    public TripScheduleController(TripScheduleService tripScheduleService) {
        this.tripScheduleService = tripScheduleService;
    }

    @GetMapping("/info")
    public List<TripSchedule> getScheduleList  (@RequestParam String tripProjectNo, @RequestParam int tripScheduleDay){
        TripSchedule tripSchedule = TripSchedule.builder()
                .tripProjectNo(tripProjectNo)
                .tripScheduleDay(tripScheduleDay)
                .build();
        return tripScheduleService.getScheduleList (tripSchedule);
    }

    @PostMapping("/info")
    public int addSchedule (@RequestBody TripProject tripProject){
        return tripScheduleService.addSchedule(tripProject);
    }

    @PutMapping("/info")
    public int modifySchedule (@RequestBody TripSchedule tripSchedule)throws ParseException {
        return tripScheduleService.modifySchedule(tripSchedule);
    }

    @PutMapping("/info/list")
    public int modifyScheduleList (@RequestBody TripProject tripProject)throws ParseException {
        return tripScheduleService.modifyScheduleList(tripProject);
    }

    @GetMapping("/info/list/date")
    public Map getScheduleListByDate (@RequestParam String tripProjectNo) {
        TripProject tripProject = TripProject.builder()
                .tripProjectNo(tripProjectNo)
                .build();
        return tripScheduleService.getScheduleListByDate(tripProject);
    }

    @DeleteMapping("/info")
    public int removeSchedule (@RequestBody Map paramMap) throws ParseException {
        return tripScheduleService.removeSchedule(paramMap);
    }
}
