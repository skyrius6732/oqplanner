package com.oqplanner.tripfavorites.service;

import com.oqplanner.tripcost.domain.TripCost;
import com.oqplanner.tripfavorites.domain.TripFavorites;
import com.oqplanner.tripfavorites.mapper.TripFavoritesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripFavoritesService {

    @Autowired
    private TripFavoritesMapper tripFavoritesMapper;

    public List<TripFavorites> getTripFavorites(TripFavorites tripFavorites){
        return tripFavoritesMapper.getTripFavorites(tripFavorites);
    }

    public int saveTripFavorites(TripFavorites tripFavorites){

        tripFavoritesMapper.saveTripFavoritesHistory(tripFavorites);

        return tripFavoritesMapper.saveTripFavorites(tripFavorites);
    }

    public int removeTripFavorites(TripFavorites tripFavorites){
        return tripFavoritesMapper.removeTripFavorites(tripFavorites);
    }

}
