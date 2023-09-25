package com.oqplanner.tripschedule.controller;



import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripschedule.domain.TripSchedule;
import com.oqplanner.tripschedule.service.TripScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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
    public List<TripSchedule> getScheduleList  (@RequestBody TripSchedule tripSchedule){
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
}
