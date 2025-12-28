package com.cigi.pickthem.domain.dtos.auth;

import com.cigi.pickthem.domain.dtos.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String accessToken;
    private UserResponseDto user;
}
