package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);
}
