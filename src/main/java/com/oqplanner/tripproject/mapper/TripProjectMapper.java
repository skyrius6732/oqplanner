package com.oqplanner.tripproject.mapper;


import com.oqplanner.tripproject.domain.TripProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TripProjectMapper {
    int saveProject(TripProject tripProject);
}
