package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.impl.UserMapper;
import com.cigi.pickthem.repositories.UserRepository;
import com.cigi.pickthem.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto requestDto) {
        return userRepository.findById(Math.toIntExact(userId))
                .map(user -> {
                    //verify that email is not used befor
                    if (userRepository.existsByEmailAndIdNot(requestDto.getEmail(), userId)) {
                        throw new IllegalArgumentException("Email Used Before");
                    }

                    //update username and email
                    user.setUsername(requestDto.getUsername());
                    user.setEmail(requestDto.getEmail());

                    UserEntity updatedUser = userRepository.save(user);

                    return userMapper.toDto(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User Not Found with id " + userId));
    }

}