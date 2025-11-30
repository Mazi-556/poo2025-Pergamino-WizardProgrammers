package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateParticipantRequestDTO dto) {
    try {
        Participant p = new Participant();
        p.setName(dto.getName());
        p.setSurname(dto.getSurname());
        p.setDni(dto.getDni());
        p.setEmail(dto.getEmail());
        p.setPassword(dto.getPassword());

        participantService.create(p);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Participante creado correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage()); 
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    }
    
    @PostMapping(path = "/auth", produces = "application/json")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDTO dto) {
    try {
        Participant tmp = new Participant();
        tmp.setEmail(dto.getEmail());
        tmp.setPassword(dto.getPassword());

        String token = authenticationService.authenticate(tmp);
        return ResponseEntity.ok(Map.of("token", token));
    } catch (Exception e) {
        return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
    }
  }
}
