package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CreateParticipantRequestDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.AuthenticationService;
import ar.edu.unnoba.poo2025.torneos.service.ParticipantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participants")
public class ParticipantResource {

    private final ParticipantService participantService;
    private final AuthenticationService authenticationService;

    public ParticipantResource(ParticipantService participantService, AuthenticationService authenticationService) {
        this.participantService = participantService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/account", produces = "application/json")
    public ResponseEntity<?> create(@Valid @RequestBody CreateParticipantRequestDTO dto) {
        Participant p = new Participant();
        p.setName(dto.getName());
        p.setSurname(dto.getSurname());
        p.setDni(dto.getDni());
        p.setEmail(dto.getEmail());
        p.setPassword(dto.getPassword());

        participantService.create(p);

        // Si falla (email duplicado), se lanza el ResourceAlreadyExistsException -> GlobalHandler devuelve 409
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Participante creado correctamente"));
    }
    
    @PostMapping(path = "/auth", produces = "application/json")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDTO dto) {
        Participant tmp = new Participant();
        tmp.setEmail(dto.getEmail());
        tmp.setPassword(dto.getPassword());

        // Si falla (password mal), lanzamos el UnauthorizedException -> GlobalHandler devuelve 401
        String token = authenticationService.authenticate(tmp);
        
        return ResponseEntity.ok(Map.of("token", token));
    }
}