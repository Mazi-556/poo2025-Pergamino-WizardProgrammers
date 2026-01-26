package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;

@RestController
@RequestMapping("/registrations") 
public class RegistrationResource {

    private final RegistrationService registrationService;
    private final AuthorizationService authorizationService;

    public RegistrationResource(RegistrationService registrationService, AuthorizationService authorizationService) {
        this.registrationService = registrationService;
        this.authorizationService = authorizationService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getMyInscriptions(@RequestHeader("Authorization") String token) {
        // authorize lanzará UnauthorizedException si el token falla
        Participant p = authorizationService.authorize(token);
        
        List<ParticipantRegistrationDTO> dtos = registrationService.getParticipantRegistrations(p.getIdParticipant());
        return ResponseEntity.ok(dtos);
    }


    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getInscriptionDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id) {
        
        Participant p = authorizationService.authorize(token);
        
        // getRegistrationDetail va a lanzar una UnauthorizedException si la inscripcion no es del usuario
        ParticipantRegistrationDetailDTO dto = registrationService.getRegistrationDetail(id, p.getIdParticipant());
        return ResponseEntity.ok(dto);
    }
}