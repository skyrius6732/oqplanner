package com.oqplanner.common.controller;

import com.oqplanner.common.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// Spring Framework 예제
@RestController
@RequestMapping("/common")
public class SessionController {

    @GetMapping("/api/getSessionValue")
    public ResponseEntity<Map> getSessionValue(HttpServletRequest request) {
        // 세션에서 값을 가져옵니다.
        HttpSession session = request.getSession();
        Map map = new HashMap<>();
        String userNoSession = (String) session.getAttribute(SessionConst.TRIP_USER_NO);
        String projectNoSession = (String) session.getAttribute(SessionConst.TRIP_PROJECT_NO);
        map.put("userNoSession", userNoSession);
        map.put("projectNoSession", projectNoSession);

        // 세션 값 응답
        return ResponseEntity.ok(map);
    }
}
