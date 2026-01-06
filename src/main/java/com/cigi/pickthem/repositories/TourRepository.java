package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<TourEntity, Long> {
    boolean existsByName(String name);
}
