package com.oqplanner.tripuser.service;

import com.oqplanner.tripuser.domain.TripUser;
import com.oqplanner.tripuser.mapper.TripUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripUserService {

    @Autowired
    private TripUserMapper tripUserMapper;


    public int saveUserInfo(TripUser tripUser) {

        // user 자동 생성 후 세션값에 UUID 등록
        String uuid = UUID.randomUUID().toString();
        String tripUserNo = uuid.split("-")[uuid.split("-").length-1];
        tripUser.setTripUserNo(tripUserNo);

        return tripUserMapper.saveUserInfo(tripUser);
    }

    public int removeUserInfoBytripUserNo(String tripUserNo){
        return tripUserMapper.removeUserInfoBytripUserNo(tripUserNo);
    }

}
