package com.cigi.pickthem.domain.DTO;

import com.cigi.pickthem.domain.enums.TourStatus;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Le nom du tour est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    @NotNull(message = "Le statut du tour est obligatoire")
    private TourStatus status;
}