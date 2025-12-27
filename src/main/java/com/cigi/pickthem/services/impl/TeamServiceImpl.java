package com.cigi.pickthem.services.impl;

import com.cigi.pickthem.domain.DTO.TeamDTO;
import com.cigi.pickthem.domain.entities.TeamEntity;
import com.cigi.pickthem.mappers.TeamMapper;
import com.cigi.pickthem.repositories.TeamRepository;
import com.cigi.pickthem.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;


    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        TeamEntity saved = teamRepository.save(teamMapper.toEntity(teamDTO));
        return teamMapper.toDTO(saved);
    }
    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toDTO)
                .toList();
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        TeamEntity team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return teamMapper.toDTO(team);
    }
    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        TeamEntity existing = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        existing.setName(teamDTO.getName());
        existing.setImageUrl(teamDTO.getImageUrl());

        TeamEntity updated = teamRepository.save(existing);
        return teamMapper.toDTO(updated);
    }

    @Override
    public boolean deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            return false;
        }
        teamRepository.deleteById(id);
        return true;
    }

}
