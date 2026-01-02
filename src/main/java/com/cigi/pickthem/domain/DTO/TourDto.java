package com.cigi.pickthem.domain.DTO;

import com.cigi.pickthem.domain.enums.TourStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourDto {
    private Long id;
    private String name;
    private TourStatus status;
}