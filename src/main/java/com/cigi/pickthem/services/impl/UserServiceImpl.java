package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.dtos.UserUpdateRequestDto;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.impl.UserMapper;
import com.cigi.pickthem.repositories.UserRepository;
import com.cigi.pickthem.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        return userRepository.findById(userId)
                .map(user -> {
                    //verify that email is not used befor
//                    if (userRepository.existsByEmailAndIdNot(requestDto.getEmail(), userId)) {
//                        throw new IllegalArgumentException("Email Used Before");
//                    }

                    //update username and email
                    user.setUsername(requestDto.getUsername());
//                    user.setEmail(requestDto.getEmail());

                    UserEntity updatedUser = userRepository.save(user);

                    return userMapper.toDto(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User Not Found with id " + userId));
    }

    @Override
    public UserResponseDto deleteUser(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User Not Found with id : " + userId));
        userRepository.delete(user);

        return userMapper.toDto(user);
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toResponseDtos(users);
    }

    @Override
    public int getTotalPoints(Long userId) {
        return 0;
    }

    @Override
    public List<UserResponseDto> getTopUsers(int limit) {
        return List.of();
    }
}