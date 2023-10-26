package com.oqplanner.tripplan.controller;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripplan.service.TripPlanService;
import com.oqplanner.tripuser.service.TripUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="trip-plan-controller",  description = "여행 계획 Controller 입니다.")
@RestController
@RequestMapping("/trip/plan")
public class TripPlanController {

    private final TripPlanService tripPlanService;


    @Autowired
    public TripPlanController(TripUserService tripUserService, TripPlanService tripPlanService) {
        this.tripPlanService = tripPlanService;
    }

    @Operation(summary = "여행계획 저장", description = "여행계획 저장 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행계획 정보 저장 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/info")
    public int savePlanInfo(@RequestBody TripPlan tripPlan) {
        return tripPlanService.savePlanInfo(tripPlan);
    }

    @Operation(summary = "여행계획 리스트 조회", description = "여행계획 리스트 조회 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행계획 리스트 조회 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/info/list")
    public List<TripPlan> getPlanInfoList(@RequestBody TripPlan tripPlan) {
        return tripPlanService.getPlanInfoList(tripPlan);
    }

    @Operation(summary = "여행계획 조회", description = "여행계획 조회 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행계획 조회 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/info")
    public TripPlan getPlanInfo(@RequestParam String tripPlanNm, @RequestParam String tripPlannerNm) {
        TripPlan tripPlan = TripPlan.builder()
                .tripPlanNm(tripPlanNm)
                .tripPlannerNm(tripPlannerNm)
                .build();
        return tripPlanService.getPlanInfo(tripPlan);
    }


    @Operation(summary = "여행계획 삭제", description = "여행계획 삭제 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행계획 삭제 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/info")
    public int removePlanInfo(@RequestBody TripPlan tripPlan){
        return tripPlanService.removePlanInfo(tripPlan);
    }


    @Operation(summary = "여행계획 삭제", description = "여행계획 삭제 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행계획 삭제 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/info/{tripPlanNo}")
    public int removePlanInfoBytripPlanNo(@PathVariable String tripPlanNo){
        return tripPlanService.removePlanInfoBytripPlanNo(tripPlanNo);
    }





}
