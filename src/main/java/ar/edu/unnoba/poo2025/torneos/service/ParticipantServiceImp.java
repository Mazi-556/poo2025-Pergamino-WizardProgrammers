package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.Repository.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;

@Service
public class ParticipantServiceImp implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(Participant participant) throws Exception {
        // Verificar si el email ya existe
        Participant existingParticipant = participantRepository.findByEmail(participant.getEmail());
        if (existingParticipant != null) {
            throw new Exception("Ya existe un participante con ese email.");
        }

        // Verificar si el DNI es válido, o si en Postman se está enviando como "DNI" en mayúsculas
        if (participant.getDni() == 0) {
            throw new Exception("El DNI es inválido o nulo. Asegúrate de que la clave en el JSON sea 'dni' (en minúscula).");
        }

        // Verificar si el DNI ya existe
        Participant existingParticipantDNI = participantRepository.findByDNI(participant.getDni());
        if (existingParticipantDNI != null) {
            // Si encontramos uno, lanzamos una excepción
            throw new Exception("Ya existe un participante con ese DNI.");
        }

        // Hashear la contraseña
        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);

        // Guardar participante en la base de datos
        participantRepository.save(participant);
    }
}