package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;

@WebMvcTest(RegistrationResource.class)
public class RegistrationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private AuthorizationService authorizationService;

    private Participant participantMock;

    @BeforeEach
    void setUp() {
        participantMock = new Participant();
        participantMock.setIdParticipant(100); 
        participantMock.setEmail("alumno@unnoba.edu.ar");
    }

    @Test
    void testListarMisInscripciones() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        ParticipantRegistrationDTO inscripcion1 = new ParticipantRegistrationDTO();
        inscripcion1.setId(1);
        inscripcion1.setTournamentName("Torneo de juegos de meza");
        inscripcion1.setCompetitionName("Ajedrez");
        inscripcion1.setDate(LocalDate.now());
        inscripcion1.setPrice(500.0f);

        List<ParticipantRegistrationDTO> lista = Arrays.asList(inscripcion1);

        when(registrationService.getParticipantRegistrations(100)).thenReturn(lista);

        mockMvc.perform(get("/registrations")
                .header("Authorization", "Bearer fake-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tournamentName").value("Torneo de juegos de meza"))
                .andExpect(jsonPath("$[0].competitionName").value("Ajedrez"));
    }

    @Test
    void testObtenerDetalleInscripcion() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        ParticipantRegistrationDetailDTO detalle = new ParticipantRegistrationDetailDTO();
        detalle.setId(10);
        detalle.setTournamentName("Torneo Anual");
        detalle.setCompetitionName("Futbol");
        detalle.setPrice(1500.0);
        detalle.setRegistrationDate(LocalDate.now());

        when(registrationService.getRegistrationDetail(eq(10), eq(100))).thenReturn(detalle);

        mockMvc.perform(get("/registrations/{id}", 10)
                .header("Authorization", "Bearer fake-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.tournamentName").value("Torneo Anual"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }

    @Test
    void testTokenInvalido() throws Exception {
        when(authorizationService.authorize(anyString()))
            .thenThrow(new ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException("Token inválido"));

        mockMvc.perform(get("/registrations")
                .header("Authorization", "Bearer token-malo"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInscripcionNoEncontrada() throws Exception {
        when(authorizationService.authorize(anyString())).thenReturn(participantMock);

        when(registrationService.getRegistrationDetail(eq(999), anyInt()))
            .thenThrow(new ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException("Inscripción no encontrada"));

        mockMvc.perform(get("/registrations/{id}", 999)
                .header("Authorization", "Bearer fake-token"))
                .andExpect(status().isNotFound());
    }
}