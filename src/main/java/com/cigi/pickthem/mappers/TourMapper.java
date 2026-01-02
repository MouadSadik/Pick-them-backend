package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.DTO.TourDto;
import com.cigi.pickthem.domain.entities.TourEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TourMapper {
    TourDto toDto(TourEntity tourEntity);
    TourEntity toEntity(TourDto tourDto);
}
