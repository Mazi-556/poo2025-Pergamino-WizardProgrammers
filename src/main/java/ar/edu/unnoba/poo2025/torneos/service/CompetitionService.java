package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Registration;

public interface CompetitionService {
    
    public List<Competition> findByTournamentId(Long tournamentId);
    
    public Competition createCompetition(Long tournamentId, String name, int quota, double basePrice);

    public Competition updateCompetition(Long tournamentId, Integer competitionId, String name, int quota, double basePrice);
    
    public void deleteCompetition(Long tournamentId, Integer competitionId);
    
    public Competition findByIdAndTournament(Long tournamentId, Integer competitionId);

    public List<Registration> findRegistrationsByCompetition(Long tournamentId, Integer competitionId);
    
    public AdminCompetitionDetailDTO getCompetitionDetail(Long tournamentId, Integer competitionId);
    
    public List<CompetitionSummaryDTO> getCompetitionSummaries(Long tournamentId);
    
    public List<CompetitionSummaryDTO> getPublicCompetitions(Long tournamentId);
    
    public CompetitionSummaryDTO getPublicCompetitionDetail(Long tournamentId, Integer competitionId);
}