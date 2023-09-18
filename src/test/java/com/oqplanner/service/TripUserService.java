package com.oqplanner.service;

import com.oqplanner.domain.TripUser;
import com.oqplanner.mapper.TripUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class TripUserService {

    @Autowired
    private TripUserMapper tripUserMapper;

    public void saveUserInfo(TripUser tripUser) {
        tripUserMapper.saveUserInfo(tripUser);
    }

}
