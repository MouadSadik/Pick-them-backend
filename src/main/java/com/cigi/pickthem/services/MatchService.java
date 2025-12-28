package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.DTO.MatchDTO;
import com.cigi.pickthem.domain.entities.MatchEntity;

import java.util.List;

/**
 * @author $ {USERS}
 **/
public interface MatchService {
     MatchDTO createMatch(Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw, Long tourId);
     MatchDTO enterResult(Long matchId, int scoreA, int scoreB);
     void deleteMatch(Long matchId);
     MatchDTO updateMatch(Long matchId, Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw);
    }
