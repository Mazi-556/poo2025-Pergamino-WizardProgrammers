package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

import java.util.List;
public interface RegistrationService {

    RegistrationResponseDTO registerParticipant(Long tournamentId, Integer competitionId, Participant participant) throws Exception;
    // Método MOVIDO desde CompetitionService
    List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception;
}