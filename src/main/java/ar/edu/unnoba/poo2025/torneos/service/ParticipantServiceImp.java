package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Repository.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.xexceptions.ResourceAlreadyExistsException;

@Service
public class ParticipantServiceImp implements ParticipantService {

    @Autowired      //TODO cambiar por final y constructor
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;   

    @Override
    public void create(Participant participant) {
        // Verificar si el email ya existe
        Participant existingParticipant = participantRepository.findByEmail(participant.getEmail());
        if (existingParticipant != null) {
            throw new ResourceAlreadyExistsException("Ya existe un participante con ese email.");
        }

        //TODO:Podriamos meter una validacion para que el DNI sea de 8 digitos

        Participant existingParticipantDNI = participantRepository.findByDNI(participant.getDni());
        if (existingParticipantDNI != null) {
            throw new ResourceAlreadyExistsException("Ya existe un participante con ese DNI.");
        }
        

        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);

        participantRepository.save(participant);
    }
    @Override
    public Participant findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }
}