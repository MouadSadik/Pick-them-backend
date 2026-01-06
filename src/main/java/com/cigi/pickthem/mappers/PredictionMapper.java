package com.cigi.pickthem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cigi.pickthem.domain.dtos.predictions.PredictionResponse;
import com.cigi.pickthem.domain.entities.PredictionEntity;

@Mapper(componentModel = "spring")
public interface PredictionMapper {

    @Mapping(source = "match.id", target = "matchId")
    PredictionResponse toResponse(PredictionEntity entity);
}
