package com.cigi.pickthem.mappers;

import org.mapstruct.Mapper;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    UserResponseDto toResponse(UserEntity user);
}
