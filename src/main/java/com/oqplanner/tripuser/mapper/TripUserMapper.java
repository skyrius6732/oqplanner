package com.oqplanner.tripuser.mapper;

import com.oqplanner.tripuser.domain.TripUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TripUserMapper {
    public int saveUserInfo(TripUser tripUser);

}
