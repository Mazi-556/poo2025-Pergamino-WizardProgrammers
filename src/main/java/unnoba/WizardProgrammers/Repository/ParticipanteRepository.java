package unnoba.WizardProgrammers.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unnoba.WizardProgrammers.models.Participante;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
    
}
