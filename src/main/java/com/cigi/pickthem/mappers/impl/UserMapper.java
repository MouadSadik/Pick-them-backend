package com.cigi.pickthem.mappers.impl;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserRequestDto userDto);

    UserResponseDto toDto(UserEntity userEntity);

    List<UserResponseDto> toResponseDtos(List<UserEntity> users);

//    void updateUserFromDto(UserRequestDto dto, @MappingTarget UserEntity entity);
}