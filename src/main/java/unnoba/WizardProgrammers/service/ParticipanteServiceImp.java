package unnoba.WizardProgrammers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unnoba.WizardProgrammers.models.Participante;
import unnoba.WizardProgrammers.Repository.ParticipanteRepository;
import unnoba.WizardProgrammers.Util.PasswordEncoder;

@Service
public class ParticipanteServiceImp implements ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(Participante participante) throws Exception {
        // Verificar si el email ya existe
        Participante existente = participanteRepository.findByEmail(participante.getEmail());
        if (existente != null) {
            throw new Exception("Ya existe un participante con ese email.");
        }

        // Verificar si el DNI es válido, o si en Postman se está enviando como "DNI" en mayúsculas
        if (participante.getDni() == 0) {
            throw new Exception("El DNI es inválido o nulo. Asegúrate de que la clave en el JSON sea 'dni' (en minúscula).");
        }

        // Verificar si el DNI ya existe
        Participante existenteDNI = participanteRepository.findByDNI(participante.getDni());
        if (existenteDNI != null) {
            // Si encontramos uno, lanzamos una excepción
            throw new Exception("Ya existe un participante con ese DNI.");
        }

        // Hashear la contraseña
        String hashedPassword = passwordEncoder.encode(participante.getPassword());
        participante.setPassword(hashedPassword);

        // Guardar participante en la base de datos
        participanteRepository.save(participante);
    }
}