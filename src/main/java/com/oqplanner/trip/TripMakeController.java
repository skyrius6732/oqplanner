package com.oqplanner.trip;


import com.oqplanner.tripplan.service.TripPlanService;
import com.oqplanner.tripuser.domain.TripUser;
import com.oqplanner.tripuser.service.TripUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip/user")
public class TripMakeController {

    private final TripUserService tripUserService;
    private final TripPlanService tripPlanService;

    @Autowired
    public TripMakeController(TripUserService tripUserService, TripPlanService tripPlanService) {
        this.tripUserService = tripUserService;
        this.tripPlanService = tripPlanService;
    }

    @PostMapping("/info")
    public int saveUserInfo(@RequestBody TripUser tripUser) {
        int num = tripUserService.saveUserInfo(tripUser);
        System.out.println(num);
        return num;
    }

    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }

}
