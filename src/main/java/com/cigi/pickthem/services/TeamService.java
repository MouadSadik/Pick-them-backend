package com.cigi.pickthem.services;

import com.cigi.pickthem.domain.DTO.TeamDTO;

import java.util.List;

/**
 @author $ {USERS}
 **/public interface TeamService {
    List<TeamDTO> getAllTeams();
    TeamDTO getTeamById(Long id);
    TeamDTO createTeam(TeamDTO teamDTO);
    TeamDTO updateTeam(Long id, TeamDTO teamDTO);
    boolean deleteTeam(Long id);
}
