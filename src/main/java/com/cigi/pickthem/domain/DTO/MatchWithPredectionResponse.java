package com.cigi.pickthem.domain.DTO;

import lombok.Builder;
import lombok.Data;

/**
 * @author $ {USERS}
 **/
@Builder
@Data
public class MatchWithPredectionResponse {
    private Long matchId;
    private Integer predictedScoreA;
    private Integer predictedScoreB;
    private String teamAName;
    private String teamBName;
    private int pointsWinA;
    private int pointsWinB;
    private int pointsDraw;
    private Long tourId;
}
