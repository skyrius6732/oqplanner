package com.oqplanner.api.controller;

import com.oqplanner.api.service.ApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name="api-controller",  description = "공공데이터 API Controller 입니다.")
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;


    @GetMapping("/getLocationBasedTouristInfo")
    public Mono<String> getLocationBasedTouristInfo(
            @RequestParam String appName,
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam int radius
    ) {
        return apiService.getLocationBasedTouristInfo(appName, mapX, mapY, radius);
    }

    @GetMapping("/areaCode1")
    public Mono<String> areaCode1() {
        return apiService.areaCode1();
    }
}
