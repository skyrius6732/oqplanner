package com.oqplanner.mapper;

import com.oqplanner.domain.TripUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TripUserMapper {
    public void saveUserInfo(TripUser tripUser);

}
