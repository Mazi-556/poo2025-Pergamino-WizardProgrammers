package unnoba.WizardProgrammers.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unnoba.WizardProgrammers.models.Participante;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
    @Query("SELECT p FROM Participante p WHERE p.email = :email")
    Participante findByEmail(@Param("email") String email);
}