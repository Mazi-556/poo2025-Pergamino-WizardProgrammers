package ar.edu.unnoba.poo2025.torneos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Admin;
import ar.edu.unnoba.poo2025.torneos.service.AdminService;

@WebMvcTest(AdminAuthResource.class)
public class AdminAuthResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginExitoso() throws Exception {
        AuthenticationRequestDTO loginDTO = new AuthenticationRequestDTO();
        loginDTO.setEmail("admin@test.com");
        loginDTO.setPassword("123456");
        Admin adminEncontrado = new Admin();
        adminEncontrado.setIdAdmin(1);
        adminEncontrado.setEmail("admin@test.com");
        adminEncontrado.setName("Super Admin");

        when(adminService.authenticate("admin@test.com", "123456")).thenReturn(adminEncontrado);
        when(jwtTokenUtil.generateToken("admin@test.com")).thenReturn("fake-jwt-token-admin");

        mockMvc.perform(post("/admin/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token-admin"))
                .andExpect(jsonPath("$.user.email").value("admin@test.com"))
                .andExpect(jsonPath("$.user.role").value("admin"));
    }

    @Test
    void testLoginFallidoCredencialesInvalidas() throws Exception {
        AuthenticationRequestDTO loginDTO = new AuthenticationRequestDTO();
        loginDTO.setEmail("admin@test.com");
        loginDTO.setPassword("123456");

        when(adminService.authenticate(anyString(), anyString()))
            .thenThrow(new UnauthorizedException("Credenciales inválidas"));

        mockMvc.perform(post("/admin/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegistroExitoso() throws Exception {
        Admin nuevoAdmin = new Admin();
        nuevoAdmin.setEmail("nuevo@admin.com");
        nuevoAdmin.setPassword("123456");
        nuevoAdmin.setName("Nuevo Admin");

        Admin adminGuardado = new Admin();
        adminGuardado.setIdAdmin(5);
        adminGuardado.setEmail("nuevo@admin.com");
        adminGuardado.setName("Nuevo Admin");

        when(adminService.create(any(Admin.class))).thenReturn(adminGuardado);

        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoAdmin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAdmin").value(5))
                .andExpect(jsonPath("$.email").value("nuevo@admin.com"));
    }

    @Test
    void testRegistroFallidoEmailDuplicado() throws Exception {
        Admin nuevoAdmin = new Admin();
        nuevoAdmin.setEmail("repetido@admin.com");
        nuevoAdmin.setPassword("123456");

        when(adminService.create(any(Admin.class)))
            .thenThrow(new ResourceAlreadyExistsException("El email ya está registrado"));

        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoAdmin)))
                .andExpect(status().isConflict());
    }
}