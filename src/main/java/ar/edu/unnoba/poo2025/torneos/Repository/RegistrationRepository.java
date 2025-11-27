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
}