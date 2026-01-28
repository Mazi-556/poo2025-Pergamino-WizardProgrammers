package ar.edu.unnoba.poo2025.torneos.Repository;

import java.time.LocalDateTime; // <--- Import
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    
    List<Tournament> findByPublishedTrueAndEndDateAfter(LocalDateTime date);
    
    List<Tournament> findAllByOrderByStartDateDesc();
}