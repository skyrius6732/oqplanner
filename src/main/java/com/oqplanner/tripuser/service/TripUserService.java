package com.oqplanner.tripuser.service;

import com.oqplanner.tripuser.domain.TripUser;
import com.oqplanner.tripuser.mapper.TripUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripUserService {

    @Autowired
    private TripUserMapper tripUserMapper;


    public int saveUserInfo(TripUser tripUser) {
        return tripUserMapper.saveUserInfo(tripUser);
    }

}
