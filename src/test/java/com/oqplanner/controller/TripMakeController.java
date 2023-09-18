package com.oqplanner.controller;

import com.oqplanner.domain.TripUser;
import com.oqplanner.service.TripUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trip")
public class TripMakeController {

    private final TripUserService tripUserService;

    @Autowired
    public TripMakeController(TripUserService tripUserService) {
        this.tripUserService = tripUserService;
    }

    @GetMapping("/saveUserInfo")
    public void saveUserInfo(TripUser tripUser) {
       /* tripUser.setUserName("박정호");
        tripUser.setProjYn("Y");*/
        tripUserService.saveUserInfo(tripUser);
    }

    @GetMapping("/test")
    public void test(){
        System.out.println("ssss");
    }

}
