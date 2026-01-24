package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.CloudinaryResponse;
import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.domain.dtos.event.EventDto;
import com.cigi.pickthem.services.EventService;
import com.cigi.pickthem.services.impl.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author $ {USERS}
 **/
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final CloudinaryService cloudinaryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    // @Valid est CRUCIAL ici. Sans lui, les annotations @NotBlank du DTO sont ignorees.
    public ResponseEntity<EventDto> creeEvent(@RequestParam("name") String name,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        EventDto eventDto = EventDto.builder().
                name(name).build();

        if (file != null && !file.isEmpty()) {
            CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadImage(file);

            // Stocke URL et publicId
            eventDto.setImageUrl(cloudinaryResponse.getUrl());
            eventDto.setCloudinaryPublicId(cloudinaryResponse.getPublicId());
        }
        EventDto eventCreated = eventService.createEvent(eventDto);
        return new ResponseEntity<>(eventCreated, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllTours());
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        EventDto eventDto = EventDto.builder()
                .name(name)
                .build();

        if (file != null && !file.isEmpty()) {

            CloudinaryResponse cloudinaryResponse =
                    cloudinaryService.uploadImage(file);

            eventDto.setImageUrl(cloudinaryResponse.getUrl());
            eventDto.setCloudinaryPublicId(cloudinaryResponse.getPublicId());
        }

        EventDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event/{eventId}")
    public List<TourDto> getToursByEvent(@PathVariable Long eventId) {
        return eventService.getTourByEvent(eventId);
    }
}
