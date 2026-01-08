package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.RegistrationResponseDTO;

import java.util.List;
public interface RegistrationService {

    RegistrationResponseDTO registerParticipant(Long tournamentId, Integer competitionId, Participant participant) throws Exception;

    List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception;

    List<ParticipantRegistrationDTO> getParticipantRegistrations(Integer participantId);

    ParticipantRegistrationDetailDTO getRegistrationDetail(Integer registrationId, Integer participantId) throws Exception;
}