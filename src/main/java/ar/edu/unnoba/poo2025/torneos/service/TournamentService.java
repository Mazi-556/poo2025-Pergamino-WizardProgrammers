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

    Tournament findById(Long id);

    void deleteTournament(Long id);

    AdminTournamentDetailDTO getTournamentDetail(Long id);

    void publish (Long id);

    Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto);

}
