package ar.edu.unnoba.poo2025.torneos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ar.edu.unnoba.poo2025.torneos.Repository.AdminRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.models.Admin;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImp adminService;

    @Test
    void testCrearAdminExitoso() {
        Admin nuevo = new Admin();
        nuevo.setEmail("dario@admin.com");
        nuevo.setPassword("123456");

        when(adminRepository.existsByEmail("dario@admin.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(adminRepository.save(any(Admin.class))).thenAnswer(i -> i.getArguments()[0]);

        Admin creado = adminService.create(nuevo);
        assertNotNull(creado);
        assertEquals("hashedPassword", creado.getPassword()); 
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testCrearAdminDuplicado() {
        Admin duplicado = new Admin();
        duplicado.setEmail("admin@existe.com");

        when(adminRepository.existsByEmail("admin@existe.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            adminService.create(duplicado);
        });
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void testAutenticacionExitosa() {
        Admin adminDb = new Admin();
        adminDb.setEmail("admin@test.com");
        adminDb.setPassword("123456");

        when(adminRepository.findByEmail("admin@test.com")).thenReturn(adminDb);
        when(passwordEncoder.verify("passCorrecta", "123456")).thenReturn(true);

        Admin resultado = adminService.authenticate("admin@test.com", "passCorrecta");

        assertNotNull(resultado);
        assertEquals("admin@test.com", resultado.getEmail());
    }

    @Test
    void testAutenticacionUsuarioNoEncontrado() {
        when(adminRepository.findByEmail("usuario@noexiste.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.authenticate("usuario@noexiste.com", "passIncorrecta");
        });
    }

    @Test
    void testAutenticacionPasswordIncorrecto() {
        Admin adminDb = new Admin();
        adminDb.setEmail("admin@test.com");
        adminDb.setPassword("123456");

        when(adminRepository.findByEmail("admin@test.com")).thenReturn(adminDb);
        when(passwordEncoder.verify("passMal", "123456")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> {
            adminService.authenticate("admin@test.com", "passMal");
        });
    }

    @Test
    void testBorrarOtroAdmin() {
        Integer adminABorrar = 2;
        Integer adminSolicitante = 1;

        when(adminRepository.existsById(adminABorrar)).thenReturn(true);

        adminService.deleteAdmin(adminABorrar, adminSolicitante);

        verify(adminRepository).deleteById(adminABorrar);
    }

    @Test
    void testErrorAlBorrarseASiMismo() {
        Integer miId = 1;
        Integer solicitanteId = 1;

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            adminService.deleteAdmin(miId, solicitanteId);
        });

        assertEquals("No puedes eliminar tu propia cuenta", ex.getMessage());
        verify(adminRepository, never()).deleteById(any());
    }

    @Test
    void testBorrarAdminInexistente() {
        Integer idFantasma = 99;
        Integer solicitanteId = 1;

        when(adminRepository.existsById(idFantasma)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteAdmin(idFantasma, solicitanteId);
        });
    }
    
    @Test
    void testListarTodos() {
        Admin a1 = new Admin();
        Admin a2 = new Admin();
        when(adminRepository.findAll()).thenReturn(Arrays.asList(a1, a2));
        
        List<Admin> lista = adminService.findAll();
        
        assertEquals(2, lista.size());
    }
}