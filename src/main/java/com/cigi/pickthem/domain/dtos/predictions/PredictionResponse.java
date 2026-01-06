package com.cigi.pickthem.domain.dtos.predictions;

import com.cigi.pickthem.domain.enums.MatchResult;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PredictionResponse {

    private Long id;

    private Long matchId;

    private Integer predictedScoreA;
    private Integer predictedScoreB;

    private MatchResult predictedResult;

    private Integer points;
}
