package com.cigi.pickthem.auth.dto;

import com.cigi.pickthem.domain.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private Role role;
}
