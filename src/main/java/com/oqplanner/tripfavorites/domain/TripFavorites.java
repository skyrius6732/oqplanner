package com.oqplanner.tripfavorites.domain;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripFavorites {
    private String tripFavoritesNo;
    private String tripProjectNo;
    private String tripFavoritesNm;
    private String tripFavoritesExplan;
    private int tripFavoritesOrder;
    private String tripFavoritesOutId;
    private String tripFavoritesImgUrl;
}
