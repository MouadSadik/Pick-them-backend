package com.cigi.pickthem.domain.dtos;

import com.cigi.pickthem.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour création (avec password)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
