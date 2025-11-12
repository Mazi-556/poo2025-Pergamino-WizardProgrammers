package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.models.Tournament;

public interface TournamentService {
    List<Tournament> getPublishedTournaments();
    Tournament saveTournament(Tournament tournament);
}
