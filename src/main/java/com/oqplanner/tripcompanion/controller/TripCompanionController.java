package com.oqplanner.tripcompanion.controller;

import com.oqplanner.tripcompanion.domain.TripCompanion;
import com.oqplanner.tripcompanion.service.TripCompanionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import java.util.List;


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
    public List<TripCompanion> getCompanionList(@RequestParam String tripProjectNo){
        TripCompanion tripCompanion = TripCompanion.builder()
                .tripProjectNo(tripProjectNo).build();
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
