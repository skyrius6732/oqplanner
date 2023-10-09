package com.oqplanner.tripcompanion.controller;

import com.oqplanner.tripcompanion.domain.TripCompanion;
import com.oqplanner.tripcompanion.service.TripCompanionService;
import com.oqplanner.tripschedule.domain.TripSchedule;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Tag(name="trip-companion-controller",  description = "여행 동행자 Controller 입니다.")
@RestController
@RequestMapping("/trip/companion")
public class TripCompanionController {

    private final TripCompanionService tripCompanionService;

    public TripCompanionController(TripCompanionService tripCompanionService) {
        this.tripCompanionService = tripCompanionService;
    }

    @PostMapping("/info")
    public int saveCompanion (@RequestBody List<TripCompanion> tripCompanionList){
        return tripCompanionService.saveCompanion(tripCompanionList);
    }

    @GetMapping("/info/list")
    public List<TripCompanion> getCompanionList(@RequestBody TripCompanion tripCompanion){
        return tripCompanionService.getCompanionList(tripCompanion);
    }

    @PutMapping("/info/list")
    public int modifyCompanionList (@RequestBody List<TripCompanion> tripCompanionList) {
        return tripCompanionService.modifyCompanionList(tripCompanionList);
    }

    @DeleteMapping("/info")
    public int removeCompanion (@RequestBody TripCompanion tripCompanion){
        return tripCompanionService.removeCompanion(tripCompanion);
    }





}