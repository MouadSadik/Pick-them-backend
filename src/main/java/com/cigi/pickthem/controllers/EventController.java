package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;
import com.cigi.pickthem.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author $ {USERS}
 **/
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    // @Valid est CRUCIAL ici. Sans lui, les annotations @NotBlank du DTO sont ignorees.
    public ResponseEntity<EventDto> creeEvent(@Valid @RequestBody EventDto eventDto) {
        EventDto eventCreated = eventService.createEvent(eventDto);
        return new ResponseEntity<>(eventCreated, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllTours());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDto> updateTour(@PathVariable Long id, @Valid @RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDto));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
