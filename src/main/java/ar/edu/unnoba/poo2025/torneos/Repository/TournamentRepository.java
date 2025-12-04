package ar.edu.unnoba.poo2025.torneos.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    // Busca torneos donde published es true Y la fecha de fin (endDate) es DESPUÉS de la fecha dada
    List<Tournament> findByPublishedTrueAndEndDateAfter(LocalDate date);
    List<Tournament> findAllByOrderByStartDateDesc(); //admin, todos ordenados por fecha de inicio descendente

}
