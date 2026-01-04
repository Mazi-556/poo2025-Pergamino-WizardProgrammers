package ar.edu.unnoba.poo2025.torneos.service;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;


@Service
public class AuthorizationServiceImp implements AuthorizationService{
    private final JwtTokenUtil jwtTokenUtil;
    private final ParticipantService participantService;

    public AuthorizationServiceImp(JwtTokenUtil jwtTokenUtil,
                                ParticipantService participantService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.participantService = participantService;
    }

    @Override
    public Participant authorize(String token) throws Exception {
    if (!jwtTokenUtil.verify(token)) {
        throw new Exception("Token inválido");
    }
    String email = jwtTokenUtil.getSubject(token);
    Participant p = participantService.findByEmail(email);
    if (p == null) throw new Exception("Usuario no autorizado");
        return p;
    }
}
