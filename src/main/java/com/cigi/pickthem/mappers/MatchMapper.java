package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.dtos.match.MatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

//    @Mapping(source = "teamA.name", target = "teamAName")
//    @Mapping(source = "teamB.name", target = "teamBName")
    @Mapping(source = "tour.id", target = "tourId")
    MatchDTO toDto(MatchEntity matchEntity);

    // Optionnel : pour convertir un DTO en Entity (nécessite que TeamEntity et TourEntity soient gérés ailleurs)
    @Mapping(target = "teamA", ignore = true)  // ou configurer correctement si tu veux mapper TeamEntity
    @Mapping(target = "teamB", ignore = true)
    @Mapping(target = "tour", ignore = true)
    MatchEntity toEntity(MatchDTO matchDTO);

    // Mapping d’une liste
    List<MatchDTO> toDtos(List<MatchEntity> matchEntities);
}
