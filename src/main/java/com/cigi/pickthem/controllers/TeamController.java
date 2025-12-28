package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.DTO.TeamDTO;
import com.cigi.pickthem.services.impl.CloudinaryService;
import com.cigi.pickthem.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TeamDTO> createTeam(
            @RequestParam("name") String name,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        TeamDTO teamDTO = TeamDTO.builder()
                .name(name)
                .build();

        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            teamDTO.setImageUrl(imageUrl);
        }

        TeamDTO saved = teamService.createTeam(teamDTO);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        TeamDTO teamDTO = teamService.getTeamById(id);
        teamDTO.setName(name);

        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            teamDTO.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(teamService.updateTeam(id, teamDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        boolean deleted = teamService.deleteTeam(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

}
