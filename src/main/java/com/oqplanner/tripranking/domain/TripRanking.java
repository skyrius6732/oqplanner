package com.oqplanner.tripranking.domain;

import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripRanking {

    String tripRankingNo;
    String TripRankingFavoritesNum;
    String tripRankingOrder;
    String tripRankingRegDt;
    String tripFavoritesOutId;
    String tripFavoritesHistoryRegDt;
    String tripFavoritesImgUrl;
    String tripFavoritesNm;
    String tripRankingFlag;

}
