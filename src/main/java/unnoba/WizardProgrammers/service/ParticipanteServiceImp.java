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

        // Hashear la contraseña
        String hashedPassword = passwordEncoder.encode(participante.getPassword());
        participante.setPassword(hashedPassword);

        // Guardar participante en la base de datos
        participanteRepository.save(participante);
    }
}