package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.entities.EventEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import com.cigi.pickthem.domain.enums.TourStatus;
import com.cigi.pickthem.exception.ConflictException;
import com.cigi.pickthem.exception.NotFoundException; // Ton exception perso
import com.cigi.pickthem.mappers.TourMapper;
import com.cigi.pickthem.repositories.EventRepository;
import com.cigi.pickthem.repositories.TourRepository;
import com.cigi.pickthem.services.TourService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final EventRepository eventRepository;

    @Transactional
    public TourDto createTour(TourDto tourDto) {

        if (tourRepository.existsByNameAndEvent_Id(
                tourDto.getName(),
                tourDto.getIdEvent()
        )) {
            throw new ConflictException(
                    "Tour with name '" + tourDto.getName() +
                            "' already exists for this event."
            );
        }

        TourEntity tourEntity = tourMapper.toEntity(tourDto);

        EventEntity event = eventRepository.findById(tourDto.getIdEvent())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Event with id '" + tourDto.getIdEvent() + "' not found."
                        )
                );

        tourEntity.setEvent(event);

        TourEntity savedTour = tourRepository.save(tourEntity);
        return tourMapper.toDto(savedTour);
    }


    @Override
    public TourDto updateTour(Long id, TourDto tourDto) {
        TourEntity existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        // Validation métier pour l'update :
        // Si le nom change, on vérifie que le nouveau nom n'est pas déjà pris par quelqu'un d'autre
        if (!existingTour.getName().equals(tourDto.getName()) && tourRepository.existsByName(tourDto.getName())) {
            throw new ConflictException("Tour with name '" + tourDto.getName() + "' already existed.");
        }

        existingTour.setName(tourDto.getName());
        existingTour.setStatus(tourDto.getStatus());

        TourEntity updatedTour = tourRepository.save(existingTour);
        return tourMapper.toDto(updatedTour);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourRepository.findAll().stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourDto getTourById(Long id) {
        return tourRepository.findById(id)
                .map(tourMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Tour not found"));
    }

    @Override
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new NotFoundException("Impossible : Tour not found");
        }
        tourRepository.deleteById(id);
    }

    @Override
    public boolean isOpened(Long tourId) {
        TourEntity tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found with id : " + tourId));

        return tour.getStatus() == TourStatus.OPEN;
    }
}