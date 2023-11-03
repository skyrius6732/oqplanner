package com.oqplanner.tripfavorites.controller;


import com.oqplanner.tripcompanion.domain.TripCompanion;
import com.oqplanner.tripfavorites.domain.TripFavorites;
import com.oqplanner.tripfavorites.service.TripFavoritesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="trip-favorites-controller",  description = "여행 즐겨찾기 Controller 입니다.")
@RestController
@RequestMapping("/trip/favorites")
public class TripFavoritesController {

    private final TripFavoritesService tripFavoritesService;


    public TripFavoritesController(TripFavoritesService tripFavoritesService) {
        this.tripFavoritesService = tripFavoritesService;
    }

    @GetMapping("/info")
    public List<TripFavorites> getTripFavorites (@RequestParam String tripProjectNo){
        TripFavorites tripFavorites = TripFavorites.builder().tripProjectNo(tripProjectNo).build();
        return tripFavoritesService.getTripFavorites(tripFavorites);
    }

    @PostMapping("/info")
    public int saveTripFavorites (@RequestBody TripFavorites tripFavorites){
        return tripFavoritesService.saveTripFavorites(tripFavorites);
    }

    @DeleteMapping("/info")
    public int removeTripFavorites (@RequestBody TripFavorites tripFavorites){
        return tripFavoritesService.removeTripFavorites(tripFavorites);
    }
}
