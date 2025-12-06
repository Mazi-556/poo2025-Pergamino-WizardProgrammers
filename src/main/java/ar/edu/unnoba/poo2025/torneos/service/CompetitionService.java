package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Registration;

public interface CompetitionService {
    
    List<Competition> findByTournamentId(Long tournamentId) throws Exception;

    Competition createCompetition(Long tournamentId, String name, int quota, double basePrice) throws Exception;

    Competition updateCompetition(Long tournamentId, Integer competitionId, String name, int quota, double basePrice) throws Exception;
    
    Competition findByIdAndTournament(Long tournamentId, Integer competitionId) throws Exception;

    void deleteCompetition(Long tournamentId, Integer competitionId) throws Exception;
    
    List<Registration> findRegistrationsByCompetition(Long tournamentId, Integer competitionId) throws Exception;

    List<CompetitionSummaryDTO> getCompetitionSummaries(Long tournamentId) throws Exception;

    AdminCompetitionDetailDTO getCompetitionDetail(Long tournamentId, Integer competitionId) throws Exception;

    List<CompetitionSummaryDTO> getPublicCompetitions(Long tournamentId) throws Exception;

    CompetitionSummaryDTO getPublicCompetitionDetail(Long tournamentId, Integer competitionId) throws Exception;
}
