package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;

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

    private Admin getCurrentAdmin(String authenticationHeader) {
        jwtTokenUtil.validateToken(authenticationHeader); 
        String email = jwtTokenUtil.getSubject(authenticationHeader);
        Admin current = adminService.findByEmail(email);
        if (current == null) {
            throw new ResourceNotFoundException("Admin del token no encontrado");
        }
        return current;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<AdminTournamentSummaryDTO>> getAll(@RequestHeader("Authorization") String authenticationHeader) {
        getCurrentAdmin(authenticationHeader); 

        List<Tournament> tournaments = tournamentService.getAllOrderByStartDateDesc();
        List<AdminTournamentSummaryDTO> response = tournaments.stream()
                .map(t -> new AdminTournamentSummaryDTO(
                        t.getIdTournament(),
                        t.getName(),
                        t.getStartDate(), // LocalDateTime
                        t.getEndDate(),   // LocalDateTime
                        t.isPublished(),
                        // IMPORTANTE: El constructor necesita estos dos campos extra si no los cambiaste:
                        // totalRegistrations y totalAmount. Si tu DTO no los tiene, borralos de aca.
                        // Asumo que tu DTO tiene 7 campos segun lo que vimos antes.
                        // Si tu DTO tiene 5, borra las siguientes dos lineas:
                        (long) (t.getCompetitions() == null ? 0 : t.getCompetitions().stream().mapToLong(c -> c.getRegistrations() != null ? c.getRegistrations().size() : 0).sum()),
                        0.0 
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<AdminTournamentDetailDTO> getById(@RequestHeader("Authorization") String authenticationHeader, @PathVariable("id") Long id){
        getCurrentAdmin(authenticationHeader);
        return ResponseEntity.ok(tournamentService.getTournamentDetail(id));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<AdminTournamentSummaryDTO> create(@RequestHeader("Authorization") String authenticationHeader, @RequestBody AdminTournamentCreateUpdateDTO body){
        Admin current = getCurrentAdmin(authenticationHeader);

        Tournament t = new Tournament();
        t.setAdmin_id(current);              
        t.setName(body.getName());
        t.setDescription(body.getDescription());
        t.setStartDate(body.getStartDate());
        t.setEndDate(body.getEndDate());
        t.setPublished(false); 

        Tournament saved = tournamentService.saveTournament(t);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminTournamentSummaryDTO(
                saved.getIdTournament(),
                saved.getName(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.isPublished(),
                0L, // totalRegistrations inicial
                0.0 // totalAmount inicial
        ));
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<AdminTournamentSummaryDTO> update(
            @RequestHeader("Authorization") String authenticationHeader, 
            @PathVariable("id") Long id, 
            @RequestBody AdminTournamentCreateUpdateDTO body) {
        
        getCurrentAdmin(authenticationHeader); 
        Tournament saved = tournamentService.updateTournament(id, body);
        
        // Calculamos totales para el DTO
        long totalRegs = (saved.getCompetitions() == null) ? 0 : saved.getCompetitions().stream().mapToLong(c -> c.getRegistrations() != null ? c.getRegistrations().size() : 0).sum();
        
        return ResponseEntity.ok(new AdminTournamentSummaryDTO(
                saved.getIdTournament(),
                saved.getName(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.isPublished(),
                totalRegs,
                0.0
        ));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authenticationHeader, @PathVariable("id") Long id) {
        getCurrentAdmin(authenticationHeader);
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/published")
    public ResponseEntity<Map<String, String>> publishTournament(@RequestHeader("Authorization") String authenticationHeader, @PathVariable Long id) {
        getCurrentAdmin(authenticationHeader);
        tournamentService.publish(id);
        return ResponseEntity.ok(Map.of("message", "Torneo publicado exitosamente"));
    }
}