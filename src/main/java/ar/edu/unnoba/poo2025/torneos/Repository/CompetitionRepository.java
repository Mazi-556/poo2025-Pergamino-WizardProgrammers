package ar.edu.unnoba.poo2025.torneos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.poo2025.torneos.models.Competition;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    
}
