package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Repository.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@Service
public class ParticipantServiceImp implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    public ParticipantServiceImp(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(Participant participant) {
        // Validación de email
        if (participantRepository.findByEmail(participant.getEmail()) != null) {
            throw new ResourceAlreadyExistsException("Ya existe un participante con ese email.");
        }

        // Validación de DNI
        if (participantRepository.findByDNI(participant.getDni()) != null) {
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