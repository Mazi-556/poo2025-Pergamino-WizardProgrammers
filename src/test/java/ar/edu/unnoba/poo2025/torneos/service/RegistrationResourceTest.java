package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.controller.RegistrationResource;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.AuthorizationService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(RegistrationResource.class) // 1. Indicamos qué controlador vamos a probar
class RegistrationResourceTest {

    @Autowired
    private MockMvc mockMvc; // 2. La herramienta para simular peticiones HTTP

    @MockBean // 3. Mocks de los servicios que usa el controlador
    private RegistrationService registrationService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void getMyInscriptions_ReturnsList_WhenTokenIsValid() throws Exception {
        // A. PREPARAR DATOS (Given)
        Participant mockParticipant = new Participant();
        mockParticipant.setIdParticipant(10);
        
        // Simulamos que el token es válido y devuelve un usuario
        when(authorizationService.authorize(anyString())).thenReturn(mockParticipant);

        // Simulamos que el servicio devuelve una lista con 1 inscripción
        ParticipantRegistrationDTO dto = new ParticipantRegistrationDTO();
        dto.setId(55);
        dto.setTournamentName("Torneo Java");
        
        when(registrationService.getParticipantRegistrations(10))
                .thenReturn(List.of(dto));

        // B. EJECUTAR Y VERIFICAR (When / Then)
        mockMvc.perform(get("/inscriptions")
                .header("Authorization", "Bearer tokenFalso") // Enviamos header falso
                .contentType(MediaType.APPLICATION_JSON))
                // Verificamos que responda 200 OK
                .andExpect(status().isOk()) 
                // Verificamos que el JSON sea una lista de tamaño 1
                .andExpect(jsonPath("$", hasSize(1))) 
                // Verificamos que el campo 'tournamentName' del primer elemento sea correcto
                .andExpect(jsonPath("$[0].tournamentName", is("Torneo Java")));
    }

    @Test
    void getMyInscriptions_ReturnsUnauthorized_WhenTokenIsInvalid() throws Exception {
        // Simulamos que el servicio de autorización lanza excepción
        when(authorizationService.authorize(anyString()))
                .thenThrow(new Exception("Token inválido"));

        mockMvc.perform(get("/inscriptions")
                .header("Authorization", "Bearer tokenInvalido"))
                .andExpect(status().isUnauthorized()); // Esperamos un 401
    }
}