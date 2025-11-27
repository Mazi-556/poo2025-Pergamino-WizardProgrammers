package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.models.Tournament;

public interface TournamentService {
    //participante
    List<Tournament> getPublishedTournaments();
    Tournament saveTournament(Tournament tournament);
    //admin
    List<Tournament> getAllOrderByStartDateDesc();
    Tournament findById(Long id) throws Exception;
    void deleteTournament(Long id) throws Exception;
}
