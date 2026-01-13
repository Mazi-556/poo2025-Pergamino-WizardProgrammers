package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;

public interface RegistrationRepositoryPort {

    List<Registration> findByCompetitionIdWithParticipant(int competitionId); 

    long countByCompetitionId(int competitionId);

    boolean isParticipantRegisteredInCompetition(int participant, int competitionId);

    long countRegistrationsByParticipantAndTournament(int participant, int tournamentId);

    Registration save(Registration registration);

    List<Registration> findByParticipantId(int participantId);

    Registration findById(int registrationId);

    long countByTournamentId(Long tournamentId);
    
    Double sumPriceByTournamentId(Long tournamentId);


}
