package com.oqplanner.tripcost.controller;


import com.oqplanner.tripcost.domain.TripCost;
import com.oqplanner.tripcost.service.TripCostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="trip-cost-controller",  description = "여행 비용 Controller 입니다.")
@RestController
@RequestMapping("/trip/cost")
public class TripCostController {

    private final TripCostService tripCostService;

    public TripCostController(TripCostService tripCostService) {
        this.tripCostService = tripCostService;
    }

    @PostMapping("/private/info")
    public int savePrivateCost(@RequestBody List<TripCost> tripCostList){
        return tripCostService.savePrivateCost(tripCostList);
    }

    @PostMapping("/public/info")
    public int savePublicCost(@RequestBody List<TripCost> tripCostList){
        return tripCostService.savePublicCost(tripCostList);
    }

    @PostMapping("/info")
    public int saveCost(@RequestBody TripCost tripCost){
        return tripCostService.saveCost(tripCost);
    }

    @GetMapping("/public/info/list")
    public List<TripCost> getPublicCostList(@RequestBody TripCost tripCost){
        return tripCostService.getPublicCostList(tripCost);
    }

    @GetMapping("/private/info/list")
    public List<TripCost> getPrivateCostList(@RequestBody TripCost.TripCostRequest request){
        return tripCostService.getPrivateCostList(request);
    }

    @PutMapping("/public/info/list")
    public int modifyPublicCost(@RequestBody List<TripCost> tripCostList){
        return tripCostService.modifyPublicCost(tripCostList);
    }

    @PutMapping("/private/info/list")
    public int modifyPrivateCost(@RequestBody List<TripCost> tripCostList){
        return tripCostService.modifyPrivateCost(tripCostList);
    }

    @DeleteMapping("/public/info")
    public int removePublicCost(@RequestBody List<TripCost.TripCostRequest> tripCostList){
        return tripCostService.removePublicCost(tripCostList);
    }

    @DeleteMapping("/private/info")
    public int removePrivateCost(@RequestBody List<TripCost.TripCostRequest> tripCostList){
        return tripCostService.removePrivateCost(tripCostList);
    }

    @DeleteMapping("/info/all")
    public void removePrivateCost(@RequestBody TripCost.TripCostRequest request){
        tripCostService.removeCostAll(request);
    }







}
