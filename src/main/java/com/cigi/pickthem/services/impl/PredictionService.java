package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.dtos.predictions.*;
import com.cigi.pickthem.domain.entities.*;
import com.cigi.pickthem.domain.enums.MatchResult;
import com.cigi.pickthem.exception.BadRequestException;
import com.cigi.pickthem.exception.ForbiddenException;
import com.cigi.pickthem.exception.NotFoundException;
import com.cigi.pickthem.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final MatchRepository matchRepository;

    public PredictionEntity create(PredictionCreateRequest dto) {

        UserEntity user = getCurrentUser();

        MatchEntity match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new NotFoundException("Match not found"));

        if (predictionRepository.existsByUserIdAndMatchId(user.getId(), match.getId())) {
            throw new BadRequestException("Prediction already exists");
        }

        MatchResult predictedResult = calculateResult(dto.getPredictedScoreA(), dto.getPredictedScoreB());

        Integer points = calculatePoints(match, dto.getPredictedScoreA(), dto.getPredictedScoreB(), predictedResult);

        PredictionEntity prediction = PredictionEntity.builder()
                .user(user)
                .match(match)
                .predictedScoreA(dto.getPredictedScoreA())
                .predictedScoreB(dto.getPredictedScoreB())
                .predictedResult(predictedResult)
                .points(null)
                .calculatedForResult(null)
                .build();

        return predictionRepository.save(prediction);
    }

    public PredictionEntity update(Long id, PredictionUpdateRequest dto) {

        UserEntity user = getCurrentUser();

        PredictionEntity prediction = predictionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prediction not found"));

        if (!prediction.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Not your prediction");
        }

        prediction.setPredictedScoreA(dto.getPredictedScoreA());
        prediction.setPredictedScoreB(dto.getPredictedScoreB());

        MatchResult predictedResult = calculateResult(dto.getPredictedScoreA(), dto.getPredictedScoreB());
        prediction.setPredictedResult(predictedResult);

        // 🔥 recalcul des points
        prediction.setPoints(calculatePoints(prediction.getMatch(),
                dto.getPredictedScoreA(),
                dto.getPredictedScoreB(),
                predictedResult));

        return predictionRepository.save(prediction);
    }

    public PredictionEntity getPrediction(Long userId, Long matchId) {
        return predictionRepository.findByUserIdAndMatchId(userId, matchId)
                .orElse(null);
    }

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private MatchResult calculateResult(Integer scoreA, Integer scoreB) {
        if (scoreA > scoreB)
            return MatchResult.TEAM_A;
        if (scoreA < scoreB)
            return MatchResult.TEAM_B;
        return MatchResult.DRAW;
    }

    /**
     * Calcul des points d'une prédiction selon le match réel
     */
    public int calculatePoints(
            MatchEntity match,
            Integer predictedScoreA,
            Integer predictedScoreB,
            MatchResult predictedResult
    ) {

        if (!predictedResult.equals(match.getWinner())) {
            return 0;
        }

        if (predictedScoreA.equals(match.getScoreA())
                && predictedScoreB.equals(match.getScoreB())) {

            if (predictedResult == MatchResult.TEAM_A) {
                return match.getPointsWinA() * 2;
            } else if (predictedResult == MatchResult.TEAM_B) {
                return match.getPointsWinB() * 2;
            } else { // DRAW
                return match.getPointsDraw() * 2;
            }
        }

        if (predictedResult == MatchResult.TEAM_A) {
            return match.getPointsWinA();
        } else if (predictedResult == MatchResult.TEAM_B) {
            return match.getPointsWinB();
        } else {
            return match.getPointsDraw();
        }
    }


}
