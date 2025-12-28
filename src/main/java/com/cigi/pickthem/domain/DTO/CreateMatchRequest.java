package com.cigi.pickthem.domain.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMatchRequest {

    private Long teamAId;
    private Long teamBId;

    private int pointsWinA;
    private int pointsDraw;
    private int pointsWinB;

    private Long tourId;
}
