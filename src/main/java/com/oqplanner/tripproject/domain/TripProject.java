package com.oqplanner.tripproject.domain;


import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripProject {

    private String tripProjectNo;
    private int tripPlanNo;
    private String tripUserNo;

}
