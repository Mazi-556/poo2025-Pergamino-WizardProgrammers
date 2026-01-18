package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;
import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.CompetitionSummaryDTO;

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
