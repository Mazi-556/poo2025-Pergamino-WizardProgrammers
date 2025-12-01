package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.dto.TournamentResponseDTO;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;



@RestController
@RequestMapping("/tournaments")
public class TournamentResource {
    private final AuthorizationService authorizationService;
    private final TournamentService tournamentService;
    private final ModelMapper modelMapper;

    public TournamentResource(AuthorizationService authorizationService,
                            TournamentService tournamentService,
                            ModelMapper modelMapper) {
    this.authorizationService = authorizationService;
    this.tournamentService = tournamentService;
    this.modelMapper = modelMapper;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getTournaments(
        @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

    try {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
        return ResponseEntity.status(403).body(Map.of("error", "Falta Authorization header"));
    }

      // valida token y obtiene Participant (si necesitás el usuario)
        authorizationService.authorize(authorizationHeader);

      // obtiene torneos publicados
        List<Tournament> list = tournamentService.getPublishedTournaments();

      // mapea a DTO
        List<TournamentResponseDTO> dto = list.stream()
            .map(t -> modelMapper.map(t, TournamentResponseDTO.class))
            .toList();

        return ResponseEntity.ok(dto);

    } catch (Exception e) {
      return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
    }
    }

    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(@RequestBody Tournament newTournament) {
    try {
        Tournament saved = tournamentService.saveTournament(newTournament);

        TournamentResponseDTO response = new TournamentResponseDTO(
            saved.getIdTournament(),
            saved.getName(),
            saved.getDescripction() //Este metodo esta mal escrito xddd. mb
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
    }
} 
}
