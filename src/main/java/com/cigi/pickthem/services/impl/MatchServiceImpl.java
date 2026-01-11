package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.match.MatchDTO;
import com.cigi.pickthem.domain.dtos.match.MatchWithPredectionResponse;
import com.cigi.pickthem.domain.entities.*;
import com.cigi.pickthem.domain.enums.MatchResult;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.mappers.MatchMapper;
import com.cigi.pickthem.repositories.*;
import com.cigi.pickthem.services.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private final TourRepository tourRepository;

    @Override
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
                .orElseThrow(() -> new NotFoundException("Team A does not found"));

        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new NotFoundException("Team B does not found"));
        TourEntity tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found"));


        MatchEntity match = MatchEntity.builder()
                .teamA(teamA)
                .teamB(teamB)
                .pointsWinA(pointsWinA)
                .pointsWinB(pointsWinB)
                .pointsDraw(pointsDraw)
                .tour(tour)
                .build();

        return matchMapper.toDto(matchRepository.save(match));
    }

    @Override
    public MatchDTO enterResult(Long matchId, int scoreA, int scoreB) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found"));

        match.setScoreA(scoreA);
        match.setScoreB(scoreB);

        if (scoreA > scoreB) match.setWinner(MatchResult.TEAM_A);
        else if (scoreA < scoreB) match.setWinner(MatchResult.TEAM_B);
        else match.setWinner(MatchResult.DRAW);

        matchRepository.save(match);

        dispatchPointsForMatch(matchId);

        return matchMapper.toDto(match);
    }


    @Override
    public void deleteMatch(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match does not found"));

        matchRepository.delete(match);
    }

    @Override
    public MatchDTO updateMatch(Long matchId, Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw) {
        if(teamAId.equals(teamBId)) {
            throw new RuntimeException("Team A and Team B must be different");
        }

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match does not found"));

        TeamEntity teamA = teamRepository.findById(teamAId)
                .orElseThrow(() -> new NotFoundException("Team A does not found"));
        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new NotFoundException("Team B does not found"));

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
                .orElseThrow(() -> new NotFoundException("Match does not found"));
        return matchMapper.toDto(match);
    }



    @Transactional
    public void dispatchPointsForMatch(Long matchId) {

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found"));

        if (match.getWinner() == null) return;

        List<PredictionEntity> predictions = predictionRepository.findByMatchId(matchId);

        for (PredictionEntity prediction : predictions) {

            if (match.getWinner().equals(prediction.getCalculatedForResult())) {
                continue;
            }

            UserEntity user = prediction.getUser();

            if (prediction.getPoints() != null) {
                user.addPoints(-prediction.getPoints());
                //user.setTotalPoints(user.getTotalPoints()-prediction.getPoints());
            }

            int points = predictionService.calculatePoints(
                    match,
                    prediction.getPredictedScoreA(),
                    prediction.getPredictedScoreB(),
//                    match.getWinner()
                    prediction.getPredictedResult()
            );

            user.addPoints(points);

            prediction.setPoints(points);
            prediction.setCalculatedForResult(match.getWinner());

            userRepository.save(user);
            predictionRepository.save(prediction);
        }

        userRepository.flush();
        predictionRepository.flush();
    }



    @Override
    public List<MatchDTO> getAllMatches() {
        List<MatchEntity> matches = matchRepository.findAll();
        List<MatchDTO> matchDTOs = new ArrayList<>();
        for (MatchEntity match : matches) {
            matchDTOs.add(matchMapper.toDto(match));
        }
        return matchDTOs;
    }
//    @Override
//    public List<MatchWithPredectionResponse> getMatchsWithPredectionsByUser(Long userId) {
//
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
//        List<MatchEntity> matchs = matchRepository.findAllByUserEntity_Id(userId);
//
//        List<MatchWithPredectionResponse> responses = new ArrayList<>();
//
//        for (MatchEntity match : matchs) {
//
//            PredictionEntity prediction = predictionRepository
//                    .findByUserEntityAndMatchEntity(user, match)
//                    .orElse(null);
//
//            MatchWithPredectionResponse.MatchWithPredectionResponseBuilder builder =
//                    MatchWithPredectionResponse.builder()
//                            .matchId(match.getId())
//                            .teamAName(match.getTeamA().getName())
//                            .teamBName(match.getTeamB().getName())
//                            .pointsWinA(match.getPointsWinA())
//                            .pointsWinB(match.getPointsWinB())
//                            .pointsDraw(match.getPointsDraw());
//
//            if (prediction != null) {
//                builder
//                        .predictedScoreA(prediction.getPredictedScoreA())
//                        .predictedScoreB(prediction.getPredictedScoreB());
//            }
//
//            responses.add(builder.build());
//        }
//
//        return responses;
//    }


    @Override
    public List<MatchWithPredectionResponse> getMatchsWithPredectionsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }

        return matchRepository.findMatchesWithPredictionsByUser(userId);
    }


    @Override
    public List<MatchDTO> getMatchesByTour(Long tourId) {

        TourEntity tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        return matchRepository.findByTourId(tourId)
                .stream()
                .map(matchMapper::toDto)
                .toList();
    }


}
