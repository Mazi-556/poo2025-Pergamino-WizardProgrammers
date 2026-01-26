package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    
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
    public String authenticate(Participant incoming) {
        Participant db = participantService.findByEmail(incoming.getEmail());
        
        // aca unificamos el error para no revelar si el email existe o no (por cuestiones de seguridad)
        if (db == null || !passwordEncoder.verify(incoming.getPassword(), db.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
        
        return jwtTokenUtil.generateToken(db.getEmail());
    }
}