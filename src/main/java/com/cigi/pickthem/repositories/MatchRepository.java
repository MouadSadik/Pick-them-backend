package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $ {USERS}
 **/
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
