package ar.edu.unnoba.poo2025.torneos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unnoba.WizardProgrammers.models.Inscripcion;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    
}
