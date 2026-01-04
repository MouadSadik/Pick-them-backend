package com.cigi.pickthem.domain.dtos.users;

import com.cigi.pickthem.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private int totalPoints;

    private String imageUrl;
    private String publicId;
}
