package com.oqplanner.tripuser.controller;


import com.oqplanner.tripplan.service.TripPlanService;
import com.oqplanner.tripuser.domain.TripUser;
import com.oqplanner.tripuser.service.TripUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name="trip-make-controller",  description = "여행 생성 Controller 입니다.")
@RestController
@RequestMapping("/trip/user")
public class TripUserController {

    private final TripUserService tripUserService;
    private final TripPlanService tripPlanService;
    @Autowired
    public TripUserController(TripUserService tripUserService, TripPlanService tripPlanService) {
        this.tripUserService = tripUserService;
        this.tripPlanService = tripPlanService;
    }

    @Operation(summary = "여행유저 저장", description = "여행유저 저장 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 저장 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/info")
    public int saveUserInfo(@RequestBody TripUser tripUser)
    {
        return tripUserService.saveUserInfo(tripUser);
    }



    @Operation(summary = "여행유저 삭제", description = "여행유저 삭제 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 저장 성공", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/info/{tripUserNo}")
    public int removeUserInfoBytripUserNo(@PathVariable String tripUserNo){
        return tripUserService.removeUserInfoBytripUserNo(tripUserNo);
    }

}
