package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $ {USERS}
 **/
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByName(String name);
}
