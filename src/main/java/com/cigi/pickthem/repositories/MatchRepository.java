package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $ {USERS}
 **/
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
