package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionService;
import ar.edu.unnoba.poo2025.torneos.service.RegistrationService;

@WebMvcTest(AdminCompetitionResource.class)
public class AdminCompetitionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;
    @MockBean
    private CompetitionService competitionService;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin adminMock;

    @BeforeEach
    void setUp() {
        adminMock = new Admin();
        adminMock.setEmail("admin@test.com");
        adminMock.setIdAdmin(1);
        
        // Mock de Auth exitoso para todos los tests
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);
    }

    @Test
    void testCrearCompetencia() throws Exception {
        Long tournamentId = 1L;

        AdminCompetitionCreateUpdateDTO dto = new AdminCompetitionCreateUpdateDTO();
        dto.setName("Competencia poker");
        dto.setQuota(50);
        dto.setBasePrice(100.0);

        Competition saved = new Competition();
        saved.setIdCompetition(10);
        saved.setName("Competencia poker");
        saved.setQuota(50);
        saved.setBasePrice(100.0);

        when(competitionService.createCompetition(eq(tournamentId), anyString(), anyInt(), anyDouble()))
            .thenReturn(saved);

        mockMvc.perform(post("/admin/tournaments/{tId}/competitions", tournamentId)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Competencia poker"));
    }

    @Test
    void testListarCompetenciasDeTorneo() throws Exception {
        Long tournamentId = 1L;

        CompetitionSummaryDTO dto = new CompetitionSummaryDTO(10, "Competicion 1", 20, 500.0, 5L);
        when(competitionService.getCompetitionSummaries(tournamentId)).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/admin/tournaments/{tId}/competitions", tournamentId)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Competicion 1"))
                .andExpect(jsonPath("$[0].registrations").value(5));
    }

    @Test
    void testEliminarCompetencia() throws Exception {
        Long tournamentId = 1L;
        Integer competitionId = 10;

        doNothing().when(competitionService).deleteCompetition(tournamentId, competitionId);

        mockMvc.perform(delete("/admin/tournaments/{tId}/competitions/{cId}", tournamentId, competitionId)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerDetalleCompetencia() throws Exception {
        Long tId = 1L;
        Integer cId = 10;

        AdminCompetitionDetailDTO detalleDTO = new AdminCompetitionDetailDTO(
            cId, "Competencia Detalle", 50, 200.0, 10L, 2000.0
        );
        
        when(competitionService.getCompetitionDetail(tId, cId)).thenReturn(detalleDTO);

        mockMvc.perform(get("/admin/tournaments/{tId}/competitions/{cId}", tId, cId)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Competencia Detalle"))
                .andExpect(jsonPath("$.totalRaised").value(2000.0));
    }

    @Test
    void testActualizarCompetencia() throws Exception {
        Long tId = 1L;
        Integer cId = 10;

        AdminCompetitionCreateUpdateDTO updateDTO = new AdminCompetitionCreateUpdateDTO();
        updateDTO.setName("Competencia Actualizada");
        updateDTO.setQuota(100);
        updateDTO.setBasePrice(300.0);

        Competition competenciaActualizada = new Competition();
        competenciaActualizada.setIdCompetition(cId);
        competenciaActualizada.setName("Competencia Actualizada");
        competenciaActualizada.setQuota(100);
        competenciaActualizada.setBasePrice(300.0);
        
        when(competitionService.updateCompetition(eq(tId), eq(cId), anyString(), anyInt(), anyDouble()))
            .thenReturn(competenciaActualizada);

        when(registrationService.getCompetitionRegistrations(tId, cId))
            .thenReturn(new java.util.ArrayList<>());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/admin/tournaments/{tId}/competitions/{cId}", tId, cId)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Competencia Actualizada"))
                .andExpect(jsonPath("$.basePrice").value(300.0));
    }

    @Test
    void testListarInscriptosDeCompetencia() throws Exception {
        Long tId = 1L;
        Integer cId = 10;

        ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO inscripcion = 
            new ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO();
        
        when(registrationService.getCompetitionRegistrations(tId, cId))
            .thenReturn(java.util.Arrays.asList(inscripcion));

        mockMvc.perform(get("/admin/tournaments/{tId}/competitions/{cId}/registrations", tId, cId)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}