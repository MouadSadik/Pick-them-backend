package com.cigi.pickthem.mappers;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;
import com.cigi.pickthem.domain.entities.EventEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import org.mapstruct.Mapper;

/**
 * @author $ {USERS}
 **/
@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto toDto(EventEntity eventEntity);
    EventEntity toEntity(EventDto eventDto);
}
