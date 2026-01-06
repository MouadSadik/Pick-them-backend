package com.cigi.pickthem.domain.dtos.predictions;

import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionUpdateRequest {

    @Min(value = 0, message = "predictedScoreA must be >= 0")
    private Integer predictedScoreA;

    @Min(value = 0, message = "predictedScoreB must be >= 0")
    private Integer predictedScoreB;
}
