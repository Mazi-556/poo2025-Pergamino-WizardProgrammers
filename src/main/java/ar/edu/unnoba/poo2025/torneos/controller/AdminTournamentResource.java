package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;

@RestController
@RequestMapping("/admin/tournaments")
public class AdminTournamentResource {
    private final AdminService adminService;
    private final TournamentService tournamentService;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminTournamentResource(AdminService adminService, TournamentService tournamentService, JwtTokenUtil jwtTokenUtil) {
        this.adminService = adminService;
        this.tournamentService = tournamentService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //valida token y obtener el admin del token
    private Admin getCurrentAdmin(String authenticationHeader) throws Exception {
        jwtTokenUtil.validateToken(authenticationHeader); // verifica firma y expiración
        String email = jwtTokenUtil.getSubject(authenticationHeader);
        Admin current = adminService.findByEmail(email);
        if (current == null) {
            throw new Exception("Admin del token no encontrado");
        }
        return current;
    }




    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authenticationHeader) {
        try {
            getCurrentAdmin(authenticationHeader); //valida el token

            List<Tournament> tournaments = tournamentService.getAllOrderByStartDateDesc();
            List<AdminTournamentSummaryDTO> response = tournaments.stream()
                    .map(t -> new AdminTournamentSummaryDTO(
                            t.getIdTournament(),
                            t.getName(),
                            t.getStartDate(),
                            t.getEndDate(),
                            t.isPublished()
                    ))
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(response);        
    } catch (Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));}
    }




    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authenticationHeader, @PathVariable("id") Long id){
        try {
            getCurrentAdmin(authenticationHeader);


            AdminTournamentDetailDTO dto = tournamentService.getTournamentDetail(id);

            return ResponseEntity.ok(dto);
            
        } catch (Exception e) {
             HttpStatus status = e.getMessage() != null && e.getMessage().contains("not found")
                     ? HttpStatus.NOT_FOUND
                     : HttpStatus.UNAUTHORIZED;


            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }




    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authenticationHeader, @RequestBody AdminTournamentCreateUpdateDTO body){
        try {
            Admin current = getCurrentAdmin(authenticationHeader);

            Tournament t = new Tournament();
            t.setAdmin_id(current);               // campo Admin en tu entidad
            t.setName(body.getName());
            t.setDescripction(body.getDescription());
            t.setStartDate(body.getStartDate());
            t.setEndDate(body.getEndDate());
            t.setPublished(false); // por defecto no publicado

            Tournament saved = tournamentService.saveTournament(t);

            AdminTournamentSummaryDTO dto = new AdminTournamentSummaryDTO(
                    saved.getIdTournament(),
                    saved.getName(),
                    saved.getStartDate(),
                    saved.getEndDate(),
                    saved.isPublished()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }      
    }





    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String authenticationHeader, @PathVariable("id") Long id, @RequestBody AdminTournamentCreateUpdateDTO body) {
        try {
            getCurrentAdmin(authenticationHeader);
            Tournament t = tournamentService.findById(id);
            t.setName(body.getName());
            t.setDescripction(body.getDescription());
            t.setStartDate(body.getStartDate());
            t.setEndDate(body.getEndDate());

            Tournament saved = tournamentService.saveTournament(t);
            
            AdminTournamentSummaryDTO dto = new AdminTournamentSummaryDTO(
                    saved.getIdTournament(),
                    saved.getName(),
                    saved.getStartDate(),
                    saved.getEndDate(),
                    saved.isPublished()
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            HttpStatus status = e.getMessage() != null && e.getMessage().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));
        }
    }




    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authenticationHeader,  @PathVariable("id") Long id) {
        try{
            getCurrentAdmin(authenticationHeader);
            tournamentService.deleteTournament(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            HttpStatus status = e.getMessage() != null && e.getMessage().contains("not found")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(Map.of("error", e.getMessage()));  // Estos catch se ven sospechosos, investigar si se puede llamar a una exception
        }
    }




@PatchMapping("/{id}/published")
    public ResponseEntity<?> publishTournament(@PathVariable Long id) {
        try {
       
            tournamentService.publish(id);
            
            return ResponseEntity.ok(Map.of("message", "Torneo publicado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}