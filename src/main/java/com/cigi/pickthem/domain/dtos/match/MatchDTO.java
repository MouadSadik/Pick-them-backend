package com.cigi.pickthem.domain.dtos.match;

import com.cigi.pickthem.domain.entities.TeamEntity;
import com.cigi.pickthem.domain.enums.MatchResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchDTO {
    private Long id;
    //private String teamAName;
//    private String UrlTeamA;
    //private String teamBName;
//    private String UrlTeamB;
    private TeamEntity teamA;
    private TeamEntity teamB;

    private Integer scoreA;
    private Integer scoreB;
    private int pointsWinA;
    private int pointsWinB;
    private int pointsDraw;
    private Long tourId;
    private MatchResult winner;

}
