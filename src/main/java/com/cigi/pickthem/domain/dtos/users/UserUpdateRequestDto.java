package com.cigi.pickthem.domain.dtos.users;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {

    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    private MultipartFile image;
}