package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;

import java.util.List;
public interface RegistrationService {

    RegistrationResponseDTO registerParticipant(Long tournamentId, Integer competitionId, Participant participant) throws Exception;

    List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception;

    List<ParticipantRegistrationDTO> getParticipantRegistrations(Integer participantId);

    ParticipantRegistrationDetailDTO getRegistrationDetail(Integer registrationId, Integer participantId) throws Exception;
}