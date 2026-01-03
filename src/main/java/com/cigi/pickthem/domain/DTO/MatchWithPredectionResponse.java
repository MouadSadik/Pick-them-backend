package com.cigi.pickthem.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author $ {USERS}
 **/
@Builder
@Data
@AllArgsConstructor
public class MatchWithPredectionResponse {
    private Long matchId;
    private Integer predictedScoreA;
    private Integer predictedScoreB;
    private String teamAName;
    private String teamBName;
    private int pointsWinA;
    private int pointsWinB;
    private int pointsDraw;
    private String nameTour;
}
