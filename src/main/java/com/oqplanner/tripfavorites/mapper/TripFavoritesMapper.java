package com.oqplanner.tripfavorites.mapper;

import com.oqplanner.tripfavorites.domain.TripFavorites;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripFavoritesMapper {

    List<TripFavorites> getTripFavorites(TripFavorites tripFavorites);

    int saveTripFavorites(TripFavorites tripFavorites);

    int removeTripFavorites(TripFavorites tripFavorites);

}

