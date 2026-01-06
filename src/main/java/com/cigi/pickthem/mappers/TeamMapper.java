package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.dtos.TeamDTO;
import com.cigi.pickthem.domain.entities.TeamEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDTO(TeamEntity entity);
    TeamEntity toEntity(TeamDTO dto);
    List<TeamDTO> toDTOs(List<TeamEntity> entities);
}
