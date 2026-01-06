package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.TourDto;
import com.cigi.pickthem.services.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDto> getTourById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getTourById(id));
    }

    @PostMapping
    // @Valid est CRUCIAL ici. Sans lui, les annotations @NotBlank du DTO sont ignorees.
    public ResponseEntity<TourDto> createTour(@Valid @RequestBody TourDto tourDto) {
        TourDto createdTour = tourService.createTour(tourDto);
        return new ResponseEntity<>(createdTour, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourDto> updateTour(@PathVariable Long id, @Valid @RequestBody TourDto tourDto) {
        return ResponseEntity.ok(tourService.updateTour(id, tourDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }
}