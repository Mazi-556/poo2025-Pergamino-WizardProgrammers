package unnoba.WizardProgrammers.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unnoba.WizardProgrammers.models.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    
    
}
