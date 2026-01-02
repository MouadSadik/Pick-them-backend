package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.DTO.TourDto;
import java.util.List;

public interface TourService {
    TourDto createTour(TourDto tourDto);
    TourDto updateTour(Long id, TourDto tourDto);
    List<TourDto> getAllTours();
    TourDto getTourById(Long id);
    void deleteTour(Long id);
}