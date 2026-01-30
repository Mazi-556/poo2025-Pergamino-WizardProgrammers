package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.dto.TournamentResponseDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;

@WebMvcTest(TournamentResource.class)
public class TournamentResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private CompetitionService competitionService;

    @MockBean
    private ModelMapper modelMapper;

    private Participant participantMock;

    @BeforeEach
    void setUp() {
        participantMock = new Participant();
        participantMock.setIdParticipant(1);
        participantMock.setEmail("participante@test.com");
    }

    @Test
    void testListarTorneosPublicados() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        Tournament t1 = new Tournament();
        t1.setIdTournament(1L);
        t1.setName("Torneo Verano");
        t1.setPublished(true);

        when(tournamentService.getPublishedTournaments()).thenReturn(Arrays.asList(t1));

        TournamentResponseDTO dto = new TournamentResponseDTO();
        dto.setId(1L);
        dto.setName("Torneo Verano");
        
        when(modelMapper.map(any(Tournament.class), eq(TournamentResponseDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/tournaments")
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Torneo Verano"));
    }

    @Test
    void testListarCompetenciasDeTorneo() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        CompetitionSummaryDTO compDto = new CompetitionSummaryDTO();
        compDto.setId(10);
        compDto.setName("Competencia A");
        
        when(competitionService.getPublicCompetitions(1L)).thenReturn(Arrays.asList(compDto));

        mockMvc.perform(get("/tournaments/{id}/competitions", 1L)
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Competencia A"));
    }

    @Test
    void testObtenerDetalleCompetencia() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        CompetitionSummaryDTO detalle = new CompetitionSummaryDTO();
        detalle.setId(10);
        detalle.setName("Competencia Detalle");
        detalle.setBasePrice(500.0);

        when(competitionService.getPublicCompetitionDetail(1L, 10)).thenReturn(detalle);

        mockMvc.perform(get("/tournaments/{id}/competitions/{idComp}", 1L, 10)
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Competencia Detalle"))
                .andExpect(jsonPath("$.basePrice").value(500.0));
    }

    @Test
    void testInscripcionExitosa() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
        responseDTO.setRegistrationId(100); 
        responseDTO.setPrice(1000.0f);

        when(registrationService.registerParticipant(eq(1L), eq(10), any(Participant.class)))
            .thenReturn(responseDTO);

        mockMvc.perform(post("/tournaments/{id}/competitions/{idComp}/registrations", 1L, 10)
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRegistration").value(100)) 
                .andExpect(jsonPath("$.price").value(1000.0));
    }

    @Test
    void testAccesoSinToken() throws Exception {
        when(authorizationService.authorize(null))
            .thenThrow(new UnauthorizedException("Token requerido"));

        mockMvc.perform(get("/tournaments"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListarTorneosVacio() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);
        when(tournamentService.getPublishedTournaments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tournaments")
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}