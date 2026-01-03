package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.DTO.CreateMatchRequest;
import com.cigi.pickthem.domain.DTO.EnterResultRequest;
import com.cigi.pickthem.domain.DTO.MatchDTO;
import com.cigi.pickthem.domain.DTO.MatchWithPredectionResponse;
import com.cigi.pickthem.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchDTO> createMatch(
            @RequestBody CreateMatchRequest request
    ) {
        MatchDTO match = matchService.createMatch(
                request.getTeamAId(),
                request.getTeamBId(),
                request.getPointsWinA(),
                request.getPointsWinB(),
                request.getPointsDraw(),
                request.getTourId()
        );

        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<MatchDTO> enterResult(
            @PathVariable("id") Long matchId,
            @RequestBody EnterResultRequest request
    ) {
        MatchDTO updatedMatch = matchService.enterResult(matchId, request.getScoreA(), request.getScoreB());
        return ResponseEntity.ok(updatedMatch);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allMatch")
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    //    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<MatchWithPredectionResponse>> getMatchesWithPredictions(
//            @PathVariable Long userId
//    ) {
//        List<MatchWithPredectionResponse> matches = matchService.getMatchsWithPredectionsByUser(userId);
//        return ResponseEntity.ok(matches);
//    }
    @GetMapping("/my-predictions/{userId}")
    public ResponseEntity<List<MatchWithPredectionResponse>> getMatchesByUser(@PathVariable Long userId) {
        List<MatchWithPredectionResponse> matches = matchService.getMatchsWithPredectionsByUser(userId);
        return ResponseEntity.ok(matches);
    }
}
