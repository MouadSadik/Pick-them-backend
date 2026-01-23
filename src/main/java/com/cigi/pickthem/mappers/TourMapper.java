package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.entities.TourEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourMapper {
    @Mapping(target = "idEvent", source = "event.id")
    TourDto toDto(TourEntity tourEntity);
    TourEntity toEntity(TourDto tourDto);
}
