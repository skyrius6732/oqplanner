package com.oqplanner.domain;

import lombok.*;
import java.util.Date;


@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripUser {
    private String tripUserName;
    private String tripProjYn;
    private Date userRedDt;
    private Date userModDt;
}
