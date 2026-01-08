package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.TeamDTO;
import com.cigi.pickthem.domain.dtos.predictions.*;
import com.cigi.pickthem.domain.entities.PredictionEntity;
import com.cigi.pickthem.mappers.PredictionMapper;
import com.cigi.pickthem.services.impl.PredictionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;
    private final PredictionMapper predictionMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PredictionResponse create(@Valid @RequestBody PredictionCreateRequest request) {
        PredictionEntity prediction = predictionService.create(request);
        return predictionMapper.toResponse(prediction);
    }

    @PatchMapping("/{id}")
    public PredictionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PredictionUpdateRequest request) {
        PredictionEntity prediction = predictionService.update(id, request);
        return predictionMapper.toResponse(prediction);
    }

    @GetMapping
    public ResponseEntity<List<PredictionResponse>> getAllPredictions() {
        List<PredictionResponse> teams = predictionService.getAllPredictions();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/me")
    public ResponseEntity<List<PredictionResponse>> getUserPredictions() {
        List<PredictionResponse> teams = predictionService.getUserPredictions();
        return ResponseEntity.ok(teams);
    }

}
