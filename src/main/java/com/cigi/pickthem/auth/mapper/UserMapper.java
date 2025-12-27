package com.cigi.pickthem.auth.mapper;

import com.cigi.pickthem.auth.dto.UserResponse;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(UserEntity user);
}