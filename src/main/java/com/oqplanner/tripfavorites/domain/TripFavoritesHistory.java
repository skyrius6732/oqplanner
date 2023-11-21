package com.oqplanner.tripfavorites.domain;


import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripFavoritesHistory {
    private String tripFavoritesHistoryNo;
    private String tripFavoritesOutId;
    private String tripFavoritesHistoryRegDt;
}
