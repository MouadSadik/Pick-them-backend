package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.DTO.MatchDTO;
import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.PredictionEntity;
import com.cigi.pickthem.domain.entities.TeamEntity;
import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.domain.enums.MatchResult;
import com.cigi.pickthem.mappers.MatchMapper;
import com.cigi.pickthem.repositories.MatchRepository;
import com.cigi.pickthem.repositories.PredictionRepository;
import com.cigi.pickthem.repositories.TeamRepository;
import com.cigi.pickthem.repositories.UserRepository;
import com.cigi.pickthem.services.MatchService;
import com.cigi.pickthem.services.PredictionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author $ {USERS}
 **/
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;
    private final PredictionService predictionService;
    private final UserRepository userRepository;
    private final PredictionRepository predictionRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDTO createMatch(
            Long teamAId,
            Long teamBId,
            int pointsWinA,
            int pointsWinB,
            int pointsDraw,
            Long tourId
    ) {

        if (teamAId.equals(teamBId)) {
            throw new IllegalArgumentException("Team A and Team B must be different");
        }

        if (pointsWinA <= 0 || pointsWinB <= 0 || pointsDraw <= 0) {
            throw new IllegalArgumentException("Points must be greater than zero");
        }
        TeamEntity teamA = teamRepository.findById(teamAId)
                .orElseThrow(() -> new RuntimeException("Team A does not exist"));

        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new RuntimeException("Team B does not exist"));

        MatchEntity match = MatchEntity.builder()
                .teamA(teamA)
                .teamB(teamB)
                .pointsWinA(pointsWinA)
                .pointsWinB(pointsWinB)
                .pointsDraw(pointsDraw)
                .build();

        return matchMapper.toDto(matchRepository.save(match));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDTO enterResult(Long matchId, int scoreA, int scoreB) {

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match does not exist"));

        match.setScoreA(scoreA);
        match.setScoreB(scoreB);

        if (scoreA > scoreB) {
            match.setWinner(MatchResult.TEAM_A);
        } else if (scoreA < scoreB) {
            match.setWinner(MatchResult.TEAM_B);
        } else {
            match.setWinner(MatchResult.DRAW);
        }

        matchRepository.save(match);

        dispatchPointsForMatch(matchId);

        return matchMapper.toDto(match);
    }



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMatch(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match does not exist"));

        matchRepository.delete(match);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDTO updateMatch(Long matchId, Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw) {
        if(teamAId.equals(teamBId)) {
            throw new RuntimeException("Team A and Team B must be different");
        }

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match does not exist"));

        TeamEntity teamA = teamRepository.findById(teamAId)
                .orElseThrow(() -> new RuntimeException("Team A does not exist"));
        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new RuntimeException("Team B does not exist"));

        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setPointsWinA(pointsWinA);
        match.setPointsWinB(pointsWinB);
        match.setPointsDraw(pointsDraw);

        MatchEntity updated = matchRepository.save(match);
        return matchMapper.toDto(updated);
    }

    @Override
    public MatchDTO getMatchById(Long id) {
        MatchEntity match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match does not exist"));
        return matchMapper.toDto(match);
    }



    @Transactional
    public void dispatchPointsForMatch(Long matchId) {

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        List<PredictionEntity> predictions = predictionRepository.findByMatchId(matchId);

        for (PredictionEntity prediction : predictions) {

            if (prediction.isPointsDispatched()) {
                continue; //deja traité
            }

            UserEntity user = prediction.getUser();

            int points = predictionService.calculatePoints(
                    match,
                    prediction.getPredictedScoreA(),
                    prediction.getPredictedScoreB(),
                    match.getWinner()
            );

            user.addPoints(points);
            prediction.setPointsDispatched(true);

            userRepository.save(user);
            predictionRepository.save(prediction);
        }
    }


}
