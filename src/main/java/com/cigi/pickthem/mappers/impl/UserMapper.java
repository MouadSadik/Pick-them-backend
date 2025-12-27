package com.cigi.pickthem.mappers.impl;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // DTO Request -> Entity (création / update)
    UserEntity toEntity(UserRequestDto userDto);

    // Entity -> DTO Response (lecture, sans password)
    UserResponseDto toDto(UserEntity userEntity);

    // Conversion liste d'entities -> liste de DTO Response
    List<UserResponseDto> toResponseDtos(List<UserEntity> users);
}