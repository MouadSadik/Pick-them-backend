package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.dtos.UserUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);

    UserResponseDto deleteUser(Long id);

    Optional<UserResponseDto> getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    int getTotalPoints(Long userId);

    List<UserResponseDto> getTopUsers(int limit);

    UserResponseDto updateTotalPoints(Long userId);

}
