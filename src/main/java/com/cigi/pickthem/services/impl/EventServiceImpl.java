package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;
import com.cigi.pickthem.domain.entities.EventEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import com.cigi.pickthem.exception.ConflictException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.EventMapper;
import com.cigi.pickthem.mappers.TourMapperImpl;
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
    private final TourRepository tourRepository;
    private final TourMapperImpl tourMapperImpl;
    private final CloudinaryService cloudinaryService;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventRepository.existsByName(eventDto.getName())) {
            throw new ConflictException("Event with name '" + eventDto.getName() + "' already existed.");
        }
        EventEntity eventEntity = eventMapper.toEntity(eventDto);
        EventEntity savedevent = eventRepository.save(eventEntity);
        return eventMapper.toDto(savedevent);
    }

    @Override
    public EventDto updateEvent(Long eventId, EventDto eventDto) throws Exception {

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        // Vérifier l’unicité du nom (si modifié)
        if (!event.getName().equals(eventDto.getName())
                && eventRepository.existsByName(eventDto.getName())) {
            throw new ConflictException(
                    "Event with name '" + eventDto.getName() + "' already exists."
            );
        }

        // Mise à jour du nom
        event.setName(eventDto.getName());

        // Mise à jour de l’image si fournie
        if (eventDto.getImageUrl() != null) {

            // Supprimer l’ancienne image Cloudinary
            if (event.getCloudinaryPublicId() != null) {
                cloudinaryService.deleteImage(event.getCloudinaryPublicId());
            }

            event.setImageUrl(eventDto.getImageUrl());
            event.setCloudinaryPublicId(eventDto.getCloudinaryPublicId());
        }

        EventEntity updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }


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

    @Override
    public List<TourDto> getTourByEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        return tourRepository.findByEventId(eventId)
                .stream()
                .map(tourMapperImpl::toDto)
                .toList();    }
}
