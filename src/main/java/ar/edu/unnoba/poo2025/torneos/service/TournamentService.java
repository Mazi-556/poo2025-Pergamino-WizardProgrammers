package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO; 
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;

public interface TournamentService {
    //participante
    List<Tournament> getPublishedTournaments();
    
    Tournament saveTournament(Tournament tournament);

    List<Tournament> getAllOrderByStartDateDesc();

    Tournament findById(Long id) throws Exception;

    void deleteTournament(Long id) throws Exception;

    AdminTournamentDetailDTO getTournamentDetail(Long id) throws Exception;

    void publish (Long id) throws Exception;

    Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto) throws Exception;
}
