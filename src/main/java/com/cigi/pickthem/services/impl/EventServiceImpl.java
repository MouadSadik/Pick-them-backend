package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;
import com.cigi.pickthem.domain.entities.EventEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import com.cigi.pickthem.exception.ConflictException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.EventMapper;
import com.cigi.pickthem.repositories.EventRepository;
import com.cigi.pickthem.repositories.TourRepository;
import com.cigi.pickthem.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author $ {USERS}
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventRepository.existsByName(eventDto.getName())) {
            throw new ConflictException("Tour with name '" + eventDto.getName() + "' already existed.");
        }
        EventEntity eventEntity = eventMapper.toEntity(eventDto);
        EventEntity savedTour = eventRepository.save(eventEntity);
        return eventMapper.toDto(savedTour);    }

    @Override
    public EventDto updateEvent(Long id, EventDto eventDto) {
        EventEntity existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        // Validation métier pour l'update :
        // Si le nom change, on vérifie que le nouveau nom n'est pas déjà pris par quelqu'un d'autre
        if (!existingEvent.getName().equals(eventDto.getName()) && eventRepository.existsByName(eventDto.getName())) {
            throw new ConflictException("Event with name '" + eventDto.getName() + "' already existed.");
        }

        existingEvent.setName(eventDto.getName());

        EventEntity updatedEvent = eventRepository.save(existingEvent);
        return eventMapper.toDto(updatedEvent);    }

    @Override
    public List<EventDto> getAllTours() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());    }

    @Override
    public EventDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Tour not found"));    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Impossible : Event not found");
        }
        eventRepository.deleteById(id);
    }
}
