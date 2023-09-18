package com.oqplanner.tripuser.domain;


import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripUser {

    private String tripUserNo;
    private String tripUserName;
    private String tripProjYn;
    private Date tripUserRedDt;
    private Date tripUserModDt;
}
