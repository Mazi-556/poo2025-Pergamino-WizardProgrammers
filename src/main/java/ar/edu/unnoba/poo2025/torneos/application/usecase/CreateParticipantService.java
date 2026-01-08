package ar.edu.unnoba.poo2025.torneos.application.usecase;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.domain.ports.in.CreateParticipantUseCase;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.ParticipantRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.xexceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;

public class CreateParticipantService implements CreateParticipantUseCase {

    private final ParticipantRepositoryPort repositoryPort; //Inteccion el puerto Paricipant, no el repositorio de participant
    private final PasswordEncoder passwordEncoder;

    public CreateParticipantService(ParticipantRepositoryPort repositoryPort, PasswordEncoder passwordEncoder) {
        this.repositoryPort = repositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(Participant participant) {
        //Validar email único
        if (repositoryPort.findByEmail(participant.getEmail()) != null) {
            throw new ResourceAlreadyExistsException("Ya existe un participante con ese email.");
        }

        //Validar DNI único
        if (repositoryPort.findByDni(participant.getDni()) != null) {
            throw new ResourceAlreadyExistsException("Ya existe un participante con ese DNI.");
        }

        //Hash
        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);

        
        repositoryPort.save(participant); //Esto viene de ParticipantRepositoryPort
    }
}