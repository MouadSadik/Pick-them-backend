package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.dtos.match.MatchDTO;
import com.cigi.pickthem.domain.dtos.match.MatchWithPredectionResponse;

import java.util.List;

/**
 * @author $ {USERS}
 **/
public interface MatchService {
     MatchDTO createMatch(Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw, Long tourId);
     MatchDTO enterResult(Long matchId, int scoreA, int scoreB);
     void deleteMatch(Long matchId);
     MatchDTO updateMatch(Long matchId, Long teamAId, Long teamBId, int pointsWinA, int pointsWinB, int pointsDraw);

    MatchDTO getMatchById(Long id);
    List<MatchDTO> getAllMatches();
    //List<MatchWithPredectionResponse> getMatchsWithPredectionsByUser(Long userId);
    public List<MatchWithPredectionResponse> getMatchsWithPredectionsByUser(Long userId);

    public List<MatchDTO> getMatchesByTour(Long tourId);
    }
