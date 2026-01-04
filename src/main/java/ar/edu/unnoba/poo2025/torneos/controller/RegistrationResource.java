package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;

@RestController
@RequestMapping("/inscriptions")    //TODO cambiar en path para que quede bien con el nombre de la clase
public class RegistrationResource {

    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getMyInscriptions(@RequestHeader("Authorization") String token) {
        try {
            Participant p = authorizationService.authorize(token);
            
            List<ParticipantRegistrationDTO> dtos = registrationService.getParticipantRegistrations(p.getIdParticipant());
            
            return ResponseEntity.ok(dtos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getInscriptionDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer id) {
        try {

            Participant p = authorizationService.authorize(token);

            ParticipantRegistrationDetailDTO dto = registrationService.getRegistrationDetail(id, p.getIdParticipant());

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("no encontrada")) status = HttpStatus.NOT_FOUND;
            if (e.getMessage().contains("No tienes permiso")) status = HttpStatus.FORBIDDEN;
            
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }
}