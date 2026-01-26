package ar.edu.unnoba.poo2025.torneos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.TournamentResponseDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;

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

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getTournaments(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {

        // Validamos usuario. Si falla, el servicio lanza UnauthorizedException -> 401
        authorizationService.authorize(authorizationHeader);

        List<Tournament> list = tournamentService.getPublishedTournaments();

        List<TournamentResponseDTO> dto = list.stream()
            .map(t -> modelMapper.map(t, TournamentResponseDTO.class))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "/{tournamentId}/competitions", produces = "application/json")
    public ResponseEntity<?> getTournamentCompetitions(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable("tournamentId") Long tournamentId) {
        
        authorizationService.authorize(authorizationHeader); 
        
        List<CompetitionSummaryDTO> dtos = competitionService.getPublicCompetitions(tournamentId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(path = "/{tournamentId}/competitions/{competitionId}", produces = "application/json")
    public ResponseEntity<?> getCompetitionDetail(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("competitionId") Integer competitionId) {
        
        authorizationService.authorize(authorizationHeader);
        
        CompetitionSummaryDTO dto = competitionService.getPublicCompetitionDetail(tournamentId, competitionId);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping(path = "/{tournamentId}/competitions/{competitionId}/registrations", produces = "application/json")
    public ResponseEntity<?> registerToCompetition(
          @RequestHeader("Authorization") String authenticationHeader,
          @PathVariable("tournamentId") Long tournamentId,
          @PathVariable("competitionId") Integer competitionId) {
      
          Participant p = authorizationService.authorize(authenticationHeader);
          
          RegistrationResponseDTO dto = registrationService.registerParticipant(tournamentId, competitionId, p);
          return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}