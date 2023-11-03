package com.oqplanner.api.service;

import io.swagger.v3.core.util.Json;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Service
public class ApiService {

    public Mono<String> getLocationBasedTouristInfo(String appName, double mapX, double mapY, int radius) {
        try {
            // 인증키 설정
            String serviceKey = "LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D";

            // 서비스명 인코딩
            String encodedAppName = URLEncoder.encode(appName, "UTF-8");

            // baseUrl을 필드로 추가
            final String baseUrl = "http://apis.data.go.kr/B551011/KorService1";

            // API 호출 URL 생성
            String apiUrl = baseUrl + "/locationBasedList1?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=" + encodedAppName +
                    "&mapX=" + mapX +
                    "&mapY=" + mapY +
                    "&radius=" + radius;

            // WebClient를 사용하여 API 호출
            WebClient webClient = WebClient.create();
            Mono<String> result = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(String.class);

            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Mono.just("에러 발생");
        }
    }

    public Mono<String> areaCode1() {

            // 인증키 설정
            // String serviceKey = serviceKey;
            String serviceKey = "LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D";

            // WebClient를 사용하여 API 호출
            WebClient webClient = WebClient
                    .builder()
                    .baseUrl("http://apis.data.go.kr/B551011/KorService1")
                    .build();

            // api 요청
            Mono<String> result =
                    webClient
                            .get()
                            .uri(uriBuilder ->
                                    uriBuilder
                                            .path("/areaCode1")
                                            .queryParam("serviceKey", "LkNx3nYQrdnGGhcCNgTo9OLqxwL6zlN70%2FPd8VO9UuLid8s8Umjo5tvOjeu7we3nK%2BNSvDWS6ndRvR0Q%2F4gP2w%3D%3D")
                                            .queryParam("numOfRows", 10)
                                            .queryParam("MobileOS", "ETC")
                                            .queryParam("MobileApp", "OQPlanner")
                                            .queryParam("_type", "json")
                                            .build())
                            .retrieve()
                            .bodyToMono(String.class);


            return result;
    }

}
