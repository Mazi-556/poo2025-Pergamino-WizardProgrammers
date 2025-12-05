package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;

@RestController
@RequestMapping("/admin/tournaments/{tournamentId}/competitions")

public class AdminCompetitionResource {

    private final AdminService adminService;
    private final CompetitionService competitionService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RegistrationService registrationService;

    public AdminCompetitionResource(AdminService adminService,
                                    CompetitionService competitionService,
                                    JwtTokenUtil jwtTokenUtil, 
                                    RegistrationService registrationService) {
        this.adminService = adminService;
        this.competitionService = competitionService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.registrationService = registrationService;
    }
    private Admin getCurrentAdmin(String authenticationHeader) throws Exception { // valida el token y obtiene el token del admin 
        jwtTokenUtil.validateToken(authenticationHeader); // verifica la firma y expiración
        String email = jwtTokenUtil.getSubject(authenticationHeader);
        Admin current = adminService.findByEmail(email);
        if (current == null) {
            throw new Exception("Usuario administrador no encontrado"); //TODO Reviar si esto debe ir en un service
        }
        return current;
    }



    @GetMapping(produces = "application/json") 
    public ResponseEntity<?> getCompetitions(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId) {
        try {
            getCurrentAdmin(authenticationHeader);
            
        // El servicio ya devuelve la lista de DTOs optimizada y lista para enviar
        List<CompetitionSummaryDTO> dtoList = competitionService.getCompetitionSummaries(tournamentId); 
        
        return ResponseEntity.ok(dtoList);


        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.UNAUTHORIZED;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getCompetition(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("id") Integer competitionId) {
        try {
            getCurrentAdmin(authenticationHeader);
            
            AdminCompetitionDetailDTO dto = competitionService.getCompetitionDetail(tournamentId, competitionId);
            
            return ResponseEntity.ok(dto);


        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.UNAUTHORIZED;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createCompetition(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @RequestBody AdminCompetitionCreateUpdateDTO body) {
        try {
            getCurrentAdmin(authenticationHeader);

            Competition saved = competitionService.createCompetition(
                    tournamentId,
                    body.getName(),
                    body.getQuota(),
                    body.getBase_price()
            );

            CompetitionSummaryDTO dto = new CompetitionSummaryDTO(
                    saved.getIdCompetition(),
                    saved.getName(),
                    saved.getQuota(),
                    saved.getBase_price()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateCompetition(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("id") Integer competitionId,
            @RequestBody AdminCompetitionCreateUpdateDTO body) {
        try {
            getCurrentAdmin(authenticationHeader);

            Competition saved = competitionService.updateCompetition(
                    tournamentId,
                    competitionId,
                    body.getName(),
                    body.getQuota(),
                    body.getBase_price()
            );

            CompetitionSummaryDTO dto = new CompetitionSummaryDTO(
                    saved.getIdCompetition(),
                    saved.getName(),
                    saved.getQuota(),
                    saved.getBase_price()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    
    
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteCompetition(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("id") Integer competitionId) {
        try {
            getCurrentAdmin(authenticationHeader);

            competitionService.deleteCompetition(tournamentId, competitionId);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping(path = "/{competitionId}/inscripciones", produces = "application/json")
    public ResponseEntity<?> getCompetitionRegistrations(
            @RequestHeader("Authorization") String authenticationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("competitionId") Integer competitionId) {
        try {
            getCurrentAdmin(authenticationHeader);

                List<AdminCompetitionRegistrationDTO> dtoList = registrationService.getCompetitionRegistrations(tournamentId, competitionId);

                return ResponseEntity.ok(dtoList);

        } catch (Exception e) {
            HttpStatus status = (e.getMessage() != null &&
                    (e.getMessage().contains("no encontrado") || e.getMessage().contains("not found")))
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.UNAUTHORIZED;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
