package com.cigi.pickthem.domain.dtos.match;

import com.cigi.pickthem.domain.entities.TeamEntity;
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
    private TeamEntity teamA;
    private TeamEntity teamB;
    private int pointsWinA;
    private int pointsWinB;
    private int pointsDraw;
    private Integer scoreA;
    private Integer scoreB;
    private String nameTour;
}
