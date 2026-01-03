package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.DTO.MatchWithPredectionResponse;
import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author $ {USERS}
 **/
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    @Query("""
    SELECT new com.cigi.pickthem.domain.DTO.MatchWithPredectionResponse(
        m.id,
        p.predictedScoreA,
        p.predictedScoreB,
        m.teamA.name,
        m.teamB.name,
        m.pointsWinA,
        m.pointsWinB,
        m.pointsDraw,
        t.name
    )
    FROM MatchEntity m
    LEFT JOIN m.predictions p ON p.user.id = :userId
    JOIN m.tour t
""")
    List<MatchWithPredectionResponse> findMatchesWithPredictionsByUser(
            @Param("userId") Long userId
    );


}
