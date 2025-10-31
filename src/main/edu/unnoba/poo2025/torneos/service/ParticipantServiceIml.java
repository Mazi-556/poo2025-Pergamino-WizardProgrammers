package ar.edu.unnoba.poo2025.torneos.service;
import ar.edu.unnoba.poo2025.torneos.models.Participante; //Traemos el modelo de Participante
import ar.edu.unnoba.poo2025.torneos.service.ParticipanteService;//Traemos el servicio de participante
import unnoba.WizardProgrammers.Repository.ParticipanteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



//Cuando un Service tiene EjemploServiceIml, quiere decir que es una implementacion de una interfaz
//En este caso, ParticipantServiceIml implementa la interfaz ParticipanteService
//Basicamente es inyeccion de dependencias

//En estos tipos de services va la logica del trabajo.



@Service
public class ParticipantServiceIml implements ParticipanteService{

    @Autowired //Inyeccion de dependencias por contructor
    public ParticipantServiceIml(ParticipanteRepository participantRepository){
        this.participantRepository = participantRepository;
    }
    
    private final ParticipanteRepository participantRepository;


    @Override
    public void create(Participante participant) throws Exception{
        Participante participanteExistente = participantRepository.findByEmail(participant.getEmail());//Buscar el participante por Email

        if (participanteExistente != null) {
            throw new Exception("Ya existe participante con el email: " + participant.getEmail());
        }


    }

}
