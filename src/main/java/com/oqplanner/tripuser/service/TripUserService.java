package com.oqplanner.tripuser.service;

import com.oqplanner.common.SessionConst;
import com.oqplanner.common.controller.SessionController;
import com.oqplanner.tripuser.domain.TripUser;
import com.oqplanner.tripuser.mapper.TripUserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripUserService {

    @Autowired
    private TripUserMapper tripUserMapper;

    @Autowired
    private final HttpServletRequest request; // HttpServletRequest 주입

    public int saveUserInfo(TripUser tripUser) {

        // user 자동 생성 후 세션값에 UUID 등록
        String uuid = UUID.randomUUID().toString();
        String tripUserNo = uuid.split("-")[uuid.split("-").length-1];
        tripUser.setTripUserNo(tripUserNo);

        HttpSession session = request.getSession();

        // 세션에 UUID 저장
        session.setAttribute(SessionConst.TRIP_USER_NO, tripUserNo);

        return tripUserMapper.saveUserInfo(tripUser);
    }

    public int removeUserInfoBytripUserNo(String tripUserNo){
        return tripUserMapper.removeUserInfoBytripUserNo(tripUserNo);
    }

    public TripUser getUserInfo(String tripUserNo){
        return tripUserMapper.getUserInfo(tripUserNo);
    }

}
