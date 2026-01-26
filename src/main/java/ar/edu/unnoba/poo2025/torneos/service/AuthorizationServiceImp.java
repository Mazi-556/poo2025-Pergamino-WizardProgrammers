package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@Service
public class AuthorizationServiceImp implements AuthorizationService {

    private final JwtTokenUtil jwtTokenUtil;
    private final ParticipantService participantService;

    public AuthorizationServiceImp(JwtTokenUtil jwtTokenUtil,
                                   ParticipantService participantService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.participantService = participantService;
    }

    @Override
    public Participant authorize(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Token de autorización ausente.");
        }

        // Limpieza del prefijo "Bearer " si es que viene incluido
        String cleanToken = token;
        if (token.startsWith("Bearer ")) {
            cleanToken = token.substring(7);
        }

        // validateToken lanza UnauthorizedException si el token es invalido o expiro
        jwtTokenUtil.validateToken(cleanToken);

        String email = jwtTokenUtil.getSubject(cleanToken);
        Participant p = participantService.findByEmail(email);

        if (p == null) {
            throw new UnauthorizedException("Usuario no encontrado o no autorizado.");
        }
        
        return p;
    }
}