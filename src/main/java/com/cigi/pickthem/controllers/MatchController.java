package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.DTO.CreateMatchRequest;
import com.cigi.pickthem.domain.DTO.EnterResultRequest;
import com.cigi.pickthem.domain.DTO.MatchDTO;
import com.cigi.pickthem.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable Long id,
            @RequestBody EnterResultRequest request
    ) {
        MatchDTO match = matchService.enterResult(
                id,
                request.getScoreA(),
                request.getScoreB()
        );
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
