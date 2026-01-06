package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $ {USERS}
 **/
public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
}
