package com.oqplanner.tripproject.controller;


import com.oqplanner.tripplan.domain.TripPlan;
import com.oqplanner.tripproject.domain.TripProject;
import com.oqplanner.tripproject.service.TripProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Tag(name="trip-project-controller",  description = "여행 프로젝트 Controller 입니다.")
@RestController
@RequestMapping("/trip/project")
public class TripProjectController {

    @Autowired
    private final TripProjectService tripProjectService;

    public TripProjectController(TripProjectService tripProjectService) {
        this.tripProjectService = tripProjectService;
    }
    @Operation(summary = "여행프로젝트 저장", description = "여행프로젝트 저장 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행프로젝트 정보 저장 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/info")
    public int saveProject(@RequestBody TripProject tripProject){

        return tripProjectService.saveProject(tripProject);
    }

    @Operation(summary = "여행프로젝트 삭제", description = "여행프로젝트 삭제 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행프로젝트 삭제 저장 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/info")
    public int removeProject(@RequestBody TripProject tripProject){

        // tripProjectNo는 세션에 저장 가정
        // tripUserNo는 세션에 저장 가정
        
        Map paramMap = new HashMap();
        String tripProjectNo = tripProject.getTripProjectNo();
        TripPlan tripPlan = tripProject.getTripPlan();
        paramMap.put("tripPlanNo", tripPlan.getTripPlanNo());
        paramMap.put("tripProjectNo", tripProjectNo);

        return tripProjectService.removeProject(paramMap);
    }








}
