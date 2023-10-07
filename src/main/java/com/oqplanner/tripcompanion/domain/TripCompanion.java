package com.oqplanner.tripcompanion.domain;

import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCompanion {
    private String tripCompanionNo;
    private String tripProjectNo;
    private String tripCompanionNm;
    private String tripCompanionOrder;
    private String tripCompanionRegDt;
    private String tripCompanionModDt;

}
