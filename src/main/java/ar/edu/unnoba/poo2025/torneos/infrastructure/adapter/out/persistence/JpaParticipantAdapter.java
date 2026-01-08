package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.unnoba.poo2025.torneos.domain.ports.out.ParticipantRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;


@Component
public class JpaParticipantAdapter implements ParticipantRepositoryPort {


    private final ParticipantRepository jpaRepository;

    public JpaParticipantAdapter(ParticipantRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Participant save(Participant participant) {  
        return jpaRepository.save(participant);
    }

    @Override
    public Participant findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Participant findByDni(int dni) { //Aca mapeamos el metodo del repositorio de la infraestructura
        return jpaRepository.findByDNI(dni);//con el metodo del Repository (jpa springboot). Asi ParticipantRepositoryPort sabe que metodo usar de springboot
    }                                       //Es lo mismo con los demas metodos, solo que en este caso se llaman distinto. 



    

    

}
