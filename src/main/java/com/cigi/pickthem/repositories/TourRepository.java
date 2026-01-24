package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<TourEntity, Long> {
    boolean existsByName(String name);
    List<TourEntity> findByEventId(Long eventId);
    boolean existsByNameAndEvent_Id(String name, Long eventId);


}
