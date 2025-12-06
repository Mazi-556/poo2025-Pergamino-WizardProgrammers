package ar.edu.unnoba.poo2025.torneos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.poo2025.torneos.models.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    
    @Query("SELECT r FROM Registration r WHERE r.competition_id.idCompetition = :competitionId")
    List<Registration> findByCompetitionId(@Param("competitionId") int competitionId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.competition_id.tournament_id.idTournament = :tournamentId")
    long countByTournamentId(@Param("tournamentId") Long tournamentId);

    @Query("SELECT r FROM Registration r JOIN FETCH r.participant_id p WHERE r.competition_id.idCompetition = :competitionId")
    List<Registration> findByCompetitionIdWithParticipant(@Param("competitionId") int competitionId);


    @Query("SELECT COALESCE(SUM(r.price), 0) FROM Registration r WHERE r.competition_id.tournament_id.idTournament = :tournamentId")
    Double sumPriceByTournamentId(@Param("tournamentId") Long tournamentId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.competition_id.idCompetition = :competitionId")
    long countByCompetitionId(@Param("competitionId") Integer competitionId);

    @Query("SELECT COALESCE(SUM(r.price), 0) FROM Registration r WHERE r.competition_id.idCompetition = :competitionId")
    Double sumPriceByCompetitionId(@Param("competitionId") Integer competitionId);

    //Verificar si existe inscripción del participante 
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Registration r WHERE r.participant_id.idParticipant = :participantId AND r.competition_id.idCompetition = :competitionId")
    boolean existsByParticipantAndCompetition(@Param("participantId") Integer participantId, @Param("competitionId") Integer competitionId);

    //Contar inscripciones del participante en el mismo torneo
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.participant_id.idParticipant = :participantId AND r.competition_id.tournament_id.idTournament = :tournamentId")
    long countByParticipantAndTournament(@Param("participantId") Integer participantId, @Param("tournamentId") Long tournamentId);
    
    @Query("SELECT r FROM Registration r WHERE r.participant_id.idParticipant = :participantId")
    List<Registration> findByParticipantId(@Param("participantId") Integer participantId);
}