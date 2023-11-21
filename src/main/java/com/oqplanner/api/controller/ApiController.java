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

    // API 호출시 파라미터 값의 String 부분에 인식이 안되는것 같음
    // 현재는 프론트단에서 전체 url 호출하여 진행(Front단에서만 호출 - Back단 로직 타지 않음)
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
