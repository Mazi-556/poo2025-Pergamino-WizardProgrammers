package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;
import ar.edu.unnoba.poo2025.torneos.service.TournamentService;

@WebMvcTest(AdminTournamentResource.class)
public class AdminTournamentResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private TournamentService tournamentService;

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
    }

    @Test
    void testCrearTorneoExitoso() throws Exception {

        AdminTournamentCreateUpdateDTO dto = new AdminTournamentCreateUpdateDTO();
        dto.setName("Torneo 2026");
        dto.setDescription("Competicion de padle");
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));        
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        Tournament torneoGuardado = new Tournament();
        torneoGuardado.setIdTournament(10L);
        torneoGuardado.setName("Torneo 2026");
        torneoGuardado.setStartDate(dto.getStartDate());
        torneoGuardado.setEndDate(dto.getEndDate());
        torneoGuardado.setPublished(false);
        
        when(tournamentService.saveTournament(any(Tournament.class))).thenReturn(torneoGuardado);

        mockMvc.perform(post("/admin/tournaments")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Torneo 2026"))
                .andExpect(jsonPath("$.published").value(false));
    }

    @Test
    void testObtenerDetalleTorneo() throws Exception {
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO detailDTO = 
            new ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO(
                1L, "Torneo Test", "Desc", LocalDateTime.now(), LocalDateTime.now(), true, 10L, 1000.0, null
            );

        when(tournamentService.getTournamentDetail(anyLong())).thenReturn(detailDTO);

        mockMvc.perform(get("/admin/tournaments/{id}", 1L)
                .header("Authorization", "Bearer fake-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Torneo Test"))
                .andExpect(jsonPath("$.totalRegistrations").value(10));
    }

    @Test
    void testListarTodosLosTorneos() throws Exception {
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        Tournament t1 = new Tournament();
        t1.setIdTournament(1L);
        t1.setName("Torneo 1");
        t1.setStartDate(LocalDateTime.now());
        t1.setEndDate(LocalDateTime.now().plusDays(1));
        
        Tournament t2 = new Tournament();
        t2.setIdTournament(2L);
        t2.setName("Torneo 2");
        t2.setStartDate(LocalDateTime.now());
        t2.setEndDate(LocalDateTime.now().plusDays(1));

        when(tournamentService.getAllOrderByStartDateDesc()).thenReturn(java.util.Arrays.asList(t1, t2));

        mockMvc.perform(get("/admin/tournaments")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Torneo 1"))
                .andExpect(jsonPath("$[1].name").value("Torneo 2"));
    }

    @Test
    void testActualizarTorneo() throws Exception {
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        AdminTournamentCreateUpdateDTO dto = new AdminTournamentCreateUpdateDTO();
        dto.setName("Nombre Cambiado");
        
        Tournament torneoActualizado = new Tournament();
        torneoActualizado.setIdTournament(1L);
        torneoActualizado.setName("Nombre Cambiado");
        torneoActualizado.setStartDate(LocalDateTime.now());
        torneoActualizado.setEndDate(LocalDateTime.now().plusDays(5));
        
        when(tournamentService.updateTournament(eq(1L), any(AdminTournamentCreateUpdateDTO.class)))
            .thenReturn(torneoActualizado);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/admin/tournaments/{id}", 1L)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nombre Cambiado"));
    }

    @Test
    void testEliminarTorneo() throws Exception {
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        doNothing().when(tournamentService).deleteTournament(1L);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/admin/tournaments/{id}", 1L)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPublicarTorneo() throws Exception {
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("admin@test.com");
        when(adminService.findByEmail("admin@test.com")).thenReturn(adminMock);

        doNothing().when(tournamentService).publish(1L);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/admin/tournaments/{id}/published", 1L)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Torneo publicado exitosamente"));
    }
}
