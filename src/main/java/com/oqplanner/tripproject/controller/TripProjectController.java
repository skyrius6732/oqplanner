package com.oqplanner.tripproject.controller;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="trip-project-controller",  description = "여행 일정 Controller 입니다.")
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





}
