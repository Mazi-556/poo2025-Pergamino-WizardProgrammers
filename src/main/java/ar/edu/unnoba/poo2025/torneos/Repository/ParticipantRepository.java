package ar.edu.unnoba.poo2025.torneos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.poo2025.torneos.models.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    @Query("SELECT p FROM Participant p WHERE p.email = :email")
    Participant findByEmail(@Param("email") String email);


    @Query("SELECT p FROM Participant p WHERE p.dni = :dni")
    Participant findByDNI(@Param("dni") int dni);


}