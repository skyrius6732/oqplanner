package com.oqplanner.tripranking.domain;

import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSearch {
    String tripLogoImg;
    String tripSite;
    String tripDate;
    String tripUrl;
    String tripSubject;
}
