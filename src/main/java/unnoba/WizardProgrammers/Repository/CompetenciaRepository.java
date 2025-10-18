package unnoba.WizardProgrammers.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unnoba.WizardProgrammers.models.Competencia;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Integer> {
    
}
