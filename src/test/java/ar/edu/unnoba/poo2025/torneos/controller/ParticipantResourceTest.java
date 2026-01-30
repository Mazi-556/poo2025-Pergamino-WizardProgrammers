package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CreateParticipantRequestDTO;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.service.AuthenticationService;
import ar.edu.unnoba.poo2025.torneos.service.ParticipantService;

@WebMvcTest(ParticipantResource.class)
public class ParticipantResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearCuentaParticipante() throws Exception {
        CreateParticipantRequestDTO dto = new CreateParticipantRequestDTO();
        dto.setName("Facundo");
        dto.setSurname("Moreno");
        dto.setDni(12345678);
        dto.setEmail("Moreno@mail.com");
        dto.setPassword("123456");

        mockMvc.perform(post("/participants/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Participante creado correctamente"));
    }

    @Test
    void testLoginParticipanteExitoso() throws Exception {
        AuthenticationRequestDTO authDto = new AuthenticationRequestDTO();
        authDto.setEmail("Moreno@mail.com");
        authDto.setPassword("123456");

        when(authenticationService.authenticate(any(Participant.class))).thenReturn("fake-jwt-token");
        Participant p = new Participant();
        p.setIdParticipant(1);
        p.setName("Facundo");
        p.setSurname("Moreno");
        p.setEmail("Moreno@mail.com");
        p.setDni(12345678);
        
        when(participantService.findByEmail("Moreno@mail.com")).thenReturn(p);

        mockMvc.perform(post("/participants/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.user.email").value("Moreno@mail.com"))
                .andExpect(jsonPath("$.user.role").value("participant"));
    }

    @Test
    void testCrearParticipanteDuplicado() throws Exception {
        CreateParticipantRequestDTO dto = new CreateParticipantRequestDTO();
        dto.setName("Facundo");
        dto.setSurname("Moreno");
        dto.setDni(12345678);
        dto.setEmail("Moreno@mail.com");
        dto.setPassword("123456");

        doThrow(new ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException("El email ya está registrado"))
            .when(participantService).create(any(Participant.class));

        mockMvc.perform(post("/participants/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testLoginCredencialesInvalidas() throws Exception {
        AuthenticationRequestDTO authDto = new AuthenticationRequestDTO();
        authDto.setEmail("Moreno@mail.com");
        authDto.setPassword("Pepe2026");

        when(authenticationService.authenticate(any(Participant.class)))
            .thenThrow(new ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException("Credenciales inválidas"));

        mockMvc.perform(post("/participants/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isUnauthorized());
    }
}