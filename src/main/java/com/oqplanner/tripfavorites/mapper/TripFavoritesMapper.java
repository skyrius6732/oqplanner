package com.oqplanner.tripfavorites.mapper;

import com.oqplanner.tripfavorites.domain.TripFavorites;
import com.oqplanner.tripfavorites.domain.TripFavoritesHistory;
import com.oqplanner.tripranking.domain.TripRanking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TripFavoritesMapper {

    List<TripFavorites> getTripFavorites(TripFavorites tripFavorites);

    int saveTripFavorites(TripFavorites tripFavorites);
    int saveTripFavoritesHistory(TripFavorites tripFavorites);

    int removeTripFavorites(TripFavorites tripFavorites);

    TripRanking getTripFavoritesHistory();

}

