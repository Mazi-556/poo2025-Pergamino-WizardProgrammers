package ar.edu.unnoba.poo2025.torneos.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByPublishedTrue(); //participante
    List<Tournament> findAllByOrderByStartDateDesc(); //admin, todos ordenados por fecha de inicio descendente
}
