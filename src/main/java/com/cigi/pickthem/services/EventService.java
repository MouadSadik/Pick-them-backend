package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;

import java.util.List;

/**
 * @author $ {USERS}
 **/
public interface EventService {
    EventDto createEvent(EventDto eventDto);
    EventDto updateEvent(Long id, EventDto eventDto);
    List<EventDto> getAllTours();
    EventDto getEventById(Long id);
    void deleteEvent(Long id);

}
