package ar.edu.unnoba.poo2025.torneos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

    @Query("SELECT c FROM Competition c WHERE c.tournament_id.idTournament = :tournamentId")
    List<Competition> findByTournamentId(@Param("tournamentId") Long tournamentId);

    //TODO que raro, aca no hay una consulta para que el  repositorio elemine una competicion, sin embargo 
    //TODO funciona porque el service llama a este repository con competitionRepository.delete(competition); Sera que es automatico?
}
