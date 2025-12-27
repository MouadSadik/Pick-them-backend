package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.DTO.MatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "teamA.name", target = "teamAName")
    @Mapping(source = "teamB.name", target = "teamBName")
    @Mapping(source = "tour.id", target = "tourId")
    MatchDTO toDto(MatchEntity matchEntity);

}
