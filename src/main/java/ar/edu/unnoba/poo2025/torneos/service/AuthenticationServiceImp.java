package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@Service
public class AuthenticationServiceImp implements AuthenticationService{
    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationServiceImp(ParticipantService participantService,
                                PasswordEncoder passwordEncoder,
                                JwtTokenUtil jwtTokenUtil) {
        this.participantService = participantService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String authenticate(Participant incoming) throws Exception {
    Participant db = participantService.findByEmail(incoming.getEmail());
    if (db == null) {
        throw new Exception("Usuario no encontrado");
    }
    if (!passwordEncoder.verify(incoming.getPassword(), db.getPassword())) {
        throw new Exception("Password incorrecto");
    }
        return jwtTokenUtil.generateToken(db.getEmail());
    }
}
