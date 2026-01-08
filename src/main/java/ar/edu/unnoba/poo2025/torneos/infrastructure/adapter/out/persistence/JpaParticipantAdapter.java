package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.unnoba.poo2025.torneos.domain.ports.out.ParticipantRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.Repository.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;


@Component
public class JpaParticipantAdapter implements ParticipantRepositoryPort {


    private final ParticipantRepository jpaRepository;


    @Autowired
    public JpaParticipantAdapter(ParticipantRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    

    

}
