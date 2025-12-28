package com.cigi.pickthem.domain.dtos.predictions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionCreateRequest {

    @NotNull(message = "matchId is required")
    private Long matchId;

    @NotNull(message = "predictedScoreA is required")
    @Min(value = 0, message = "predictedScoreA must be >= 0")
    private Integer predictedScoreA;

    @NotNull(message = "predictedScoreB is required")
    @Min(value = 0, message = "predictedScoreB must be >= 0")
    private Integer predictedScoreB;
}
