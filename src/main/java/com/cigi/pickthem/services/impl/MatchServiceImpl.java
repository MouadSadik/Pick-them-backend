package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.DTO.MatchDTO;
import com.cigi.pickthem.domain.entities.MatchEntity;
import com.cigi.pickthem.domain.entities.TeamEntity;
import com.cigi.pickthem.mappers.MatchMapper;
import com.cigi.pickthem.repositories.MatchRepository;
import com.cigi.pickthem.repositories.TeamRepository;
import com.cigi.pickthem.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * @author $ {USERS}
 **/
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDTO createMatch(Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw, Long tourId) {
        if (teamAId.equals(teamBId)) {
            throw new RuntimeException("Team A and Team B must be different");
        }

        TeamEntity teamA = teamRepository.findById(teamAId)
                .orElseThrow(() -> new RuntimeException("Team A does not exist"));
        TeamEntity teamB = teamRepository.findById(teamBId)
                .orElseThrow(() -> new RuntimeException("Team B does not exist"));
//        TourEntity tour = tourRepository.findById(tourId)
//                .orElseThrow(() -> new RuntimeException("Tour does not exist"));

        MatchEntity match = MatchEntity.builder()
                .teamA(teamA)
                .teamB(teamB)
                .pointsWinA(pointsWinA)
                .pointsWinB(pointsWinB)
                .pointsDraw(pointsDraw)
                //.tour(tour)
                .build();

        MatchEntity saved = matchRepository.save(match);
        return matchMapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDTO enterResult(Long matchId, int scoreA, int scoreB) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match does not exist"));

        match.setScoreA(scoreA);
        match.setScoreB(scoreB);

        MatchEntity updated = matchRepository.save(match);
        return matchMapper.toDto(updated);
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

}
