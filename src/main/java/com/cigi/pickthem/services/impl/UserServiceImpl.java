package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.CloudinaryResponse;
import com.cigi.pickthem.domain.dtos.users.UserResponseDto;
import com.cigi.pickthem.domain.dtos.users.UserUpdateRequestDto;
import com.cigi.pickthem.domain.entities.PredictionEntity;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.impl.UserMapper;
import com.cigi.pickthem.repositories.PredictionRepository;
import com.cigi.pickthem.repositories.UserRepository;
import com.cigi.pickthem.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PredictionRepository predictionRepository;
    private final CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           PredictionRepository predictionRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.predictionRepository = predictionRepository;
        this.cloudinaryService = cloudinaryService;
    }

//    @Override
//    @Transactional
//    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
//        return userRepository.findById(userId)
//                .map(user -> {
//                    // verify that email is not used befor
//                    // if (userRepository.existsByEmailAndIdNot(requestDto.getEmail(), userId)) {
//                    // throw new IllegalArgumentException("Email Used Before");
//                    // }
//
//                    // update username and email
//                    user.setName(requestDto.getName());
//                    // user.setEmail(requestDto.getEmail());
//
//                    UserEntity updatedUser = userRepository.save(user);
//
//                    return userMapper.toDto(updatedUser);
//                })
//                .orElseThrow(() -> new RuntimeException("User Not Found with id " + userId));
//    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        return userRepository.findById(userId)
                .map(user -> {

                    // Update name
                    user.setName(requestDto.getName());

                    // Update image if provided
                    if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                        if (user.getImagePublicId() != null) {
                            try {
                                cloudinaryService.deleteImage(user.getImagePublicId());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        CloudinaryResponse response = cloudinaryService.uploadImage(requestDto.getImage());
                        user.setImageUrl(response.getUrl());
                        user.setImagePublicId(response.getPublicId());
                    }


                    UserEntity updatedUser = userRepository.save(user);

                    return userMapper.toDto(updatedUser);
                })
                .orElseThrow(() -> new RuntimeException("User Not Found with id " + userId));
    }


    @Override
    public UserResponseDto deleteUser(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found with id : " + userId));
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
        List<PredictionEntity> predictions = predictionRepository.findByUserId(userId);

        return predictions.stream()
                .mapToInt(PredictionEntity::getPoints)
                .sum();
    }

    @Override
    @Transactional
    public UserResponseDto updateTotalPoints(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        int totalPoints = predictionRepository.findByUserId(userId)
                .stream()
                .mapToInt(PredictionEntity::getPoints)
                .sum();

        user.setTotalPoints(totalPoints);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getTopUsers(int limit) {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "totalPoints"))
                .stream()
                .limit(limit)
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getTop3Users() {
        return userRepository.findTop3ByOrderByTotalPointsAsc()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

}