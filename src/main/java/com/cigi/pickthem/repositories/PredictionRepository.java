package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.PredictionEntity;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<PredictionEntity, Long> {

    Optional<PredictionEntity> findByUserIdAndMatchId(Long userId, Long matchId);

    boolean existsByUserIdAndMatchId(Integer userId, Long matchId);
    List<PredictionEntity> findByMatchId(Long matchId);
    List<PredictionEntity> findByUserId(Long userId);
//    Optional<PredictionEntity> findByUserEntityAndMatchEntity(
//            UserEntity user,
//            MatchEntity match
//    );


}
