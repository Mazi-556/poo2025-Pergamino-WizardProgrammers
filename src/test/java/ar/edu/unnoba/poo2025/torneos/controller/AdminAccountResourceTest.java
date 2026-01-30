package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;

@WebMvcTest(AdminAccountResource.class)
public class AdminAccountResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin adminLogueado;

    @BeforeEach
    void setUp() {
        adminLogueado = new Admin();
        adminLogueado.setIdAdmin(1);
        adminLogueado.setEmail("superadmin1@test.com");
        
        doNothing().when(jwtTokenUtil).validateToken(anyString());
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("superadmin1@test.com");
        when(adminService.findByEmail("superadmin1@test.com")).thenReturn(adminLogueado);
    }

    @Test
    void testListarTodosLosAdmins() throws Exception {
        Admin otroAdmin = new Admin();
        otroAdmin.setIdAdmin(2);
        otroAdmin.setEmail("superadmin2@test.com");

        when(adminService.findAll()).thenReturn(Arrays.asList(adminLogueado, otroAdmin));

        mockMvc.perform(get("/admin/accounts")
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].email").value("superadmin1@test.com"))
                .andExpect(jsonPath("$[1].email").value("superadmin2@test.com"));
    }

    @Test
    void testCrearNuevoAdmin() throws Exception {
        AuthenticationRequestDTO requestDTO = new AuthenticationRequestDTO();
        requestDTO.setEmail("nuevo@admin.com");
        requestDTO.setPassword("123456");

        Admin adminCreado = new Admin();
        adminCreado.setIdAdmin(3);
        adminCreado.setEmail("nuevo@admin.com");

        when(adminService.create(any(Admin.class))).thenReturn(adminCreado);

        mockMvc.perform(post("/admin/accounts")
                .header("Authorization", "Bearer token-valido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("nuevo@admin.com"));
    }

    @Test
    void testEliminarAdmin() throws Exception {
        Integer idAEliminar = 2;
        doNothing().when(adminService).deleteAdmin(eq(idAEliminar), eq(adminLogueado.getIdAdmin()));

        mockMvc.perform(delete("/admin/accounts/{id}", idAEliminar)
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarConTokenInvalido() throws Exception {
        doThrow(new UnauthorizedException("Token inválido"))
            .when(jwtTokenUtil).validateToken("Bearer token-malo");

        mockMvc.perform(delete("/admin/accounts/{id}", 2)
                .header("Authorization", "Bearer token-malo"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccesoConAdminNoExistente() throws Exception {
        when(jwtTokenUtil.getSubject(anyString())).thenReturn("falso@test.com");
        when(adminService.findByEmail("falso@test.com")).thenReturn(null);

        mockMvc.perform(get("/admin/accounts")
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testListarVacio() throws Exception {
        when(adminService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/accounts")
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}