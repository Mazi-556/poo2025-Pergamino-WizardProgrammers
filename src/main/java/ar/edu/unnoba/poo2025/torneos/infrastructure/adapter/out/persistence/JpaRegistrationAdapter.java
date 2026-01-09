package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.RegistrationRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaRegistrationAdapter implements RegistrationRepositoryPort {

    private final RegistrationRepository registrationRepository;

    public JpaRegistrationAdapter(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public List<Registration> findByCompetitionIdWithParticipant(int competitionId) {
        return registrationRepository.findByCompetitionIdWithParticipant(competitionId);
    }

    @Override
    public long countByCompetitionId(int competitionId) {
        return registrationRepository.countByCompetitionId(competitionId);
    }

    @Override
    public boolean isParticipantRegisteredInCompetition(int participantId, int competitionId) {
        return registrationRepository.existsByParticipantAndCompetition(participantId, competitionId);
    }

    @Override
    public long countRegistrationsByParticipantAndTournament(int participantId, int tournamentId) {
        return registrationRepository.countByParticipantAndTournament(participantId, (long) tournamentId);
    }

    @Override
    public Registration save(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public List<Registration> findByParticipantId(int participantId) {
        return registrationRepository.findByParticipantId(participantId);
    }

    @Override
    public Registration findById(int registrationId) {
        return registrationRepository.findById(registrationId).orElse(null);
    }
}