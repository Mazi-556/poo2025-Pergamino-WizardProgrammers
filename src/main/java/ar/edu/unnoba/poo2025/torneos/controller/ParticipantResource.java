package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

import ar.edu.unnoba.poo2025.torneos.dto.CreateParticipanteRequestDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.ParticipantService;

@RestController
@RequestMapping("/participant")
public class ParticipantResource {
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateParticipanteRequestDTO dto) {
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
}
