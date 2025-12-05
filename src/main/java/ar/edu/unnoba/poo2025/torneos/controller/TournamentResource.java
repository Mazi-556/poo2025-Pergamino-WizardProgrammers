package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.TournamentResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionService;
import ar.edu.unnoba.poo2025.torneos.models.Competition;



@RestController
@RequestMapping("/tournaments")
public class TournamentResource {
    private final AuthorizationService authorizationService;
    private final TournamentService tournamentService;
    private final ModelMapper modelMapper;
    private final RegistrationService registrationService;
    private final CompetitionService competitionService;

    public TournamentResource(AuthorizationService authorizationService,
                            TournamentService tournamentService,
                            ModelMapper modelMapper,
                            RegistrationService registrationService, 
                            CompetitionService competitionService) {
    this.authorizationService = authorizationService;
    this.tournamentService = tournamentService;
    this.modelMapper = modelMapper;
    this.registrationService = registrationService;
    this.competitionService = competitionService;
    }


    //TODO ordenar los metodos por GetMapping, PostMapping, etc
    @GetMapping(produces = "application/json") //TODO esto no tiene path, porque?
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


    @PostMapping(path = "/{tournamentId}/competitions/{competitionId}/registrations", produces = "application/json")
    public ResponseEntity<?> registerToCompetition(
          @RequestHeader("Authorization") String authenticationHeader, // En el PDF dice "authentication" (a veces Authorization)
          @PathVariable("tournamentId") Long tournamentId,
          @PathVariable("competitionId") Integer competitionId) {
      try {
          // 1. Validar Token y obtener Participante (Reutiliza tu servicio de Auth)
          Participant p = authorizationService.authorize(authenticationHeader);

          // 2. Delegar al servicio
          RegistrationResponseDTO dto = registrationService.registerParticipant(tournamentId, competitionId, p);

          // 3. Retornar éxito
          return ResponseEntity.status(HttpStatus.CREATED).body(dto);

      } catch (Exception e) {
          HttpStatus status = HttpStatus.BAD_REQUEST;
          if (e.getMessage().contains("no autorizado") || e.getMessage().contains("Token")) {
              status = HttpStatus.UNAUTHORIZED;
          } else if (e.getMessage().contains("no encontrado")) {
              status = HttpStatus.NOT_FOUND;
          }
          
          return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
      }
  }



    @GetMapping(path = "/{tournamentId}/competitions", produces = "application/json")
    public ResponseEntity<?> getTournamentCompetitions(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable("tournamentId") Long tournamentId) {
        try {
            authorizationService.authorize(authorizationHeader); 

            List<CompetitionSummaryDTO> dtos = competitionService.getPublicCompetitions(tournamentId);
            
            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("no se encuentra disponible")) status = HttpStatus.FORBIDDEN;
            
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(path = "/{tournamentId}/competitions/{competitionId}", produces = "application/json")
    public ResponseEntity<?> getCompetitionDetail(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable("tournamentId") Long tournamentId, //TODO aca me dice "Unnecessary variable 'tournamentId'." investigar
            @PathVariable("competitionId") Integer competitionId) {
        try {
            authorizationService.authorize(authorizationHeader);

            CompetitionSummaryDTO dto = competitionService.getPublicCompetitionDetail(tournamentId, competitionId);

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e.getMessage().contains("no encontrada")) status = HttpStatus.NOT_FOUND;
            if (e.getMessage().contains("no se encuentra disponible")) status = HttpStatus.FORBIDDEN;

            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

}
