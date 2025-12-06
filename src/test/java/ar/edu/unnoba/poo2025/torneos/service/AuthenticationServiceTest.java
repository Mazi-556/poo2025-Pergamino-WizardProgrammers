package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private ParticipantService participantService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationServiceImp authService;

    @Test
    void authenticate_Success() throws Exception {
        // Datos
        Participant inputUser = new Participant();
        inputUser.setEmail("test@mail.com");
        inputUser.setPassword("plainPassword");

        Participant dbUser = new Participant();
        dbUser.setEmail("test@mail.com");
        dbUser.setPassword("hashedPassword");

        // Mocks
        when(participantService.findByEmail("test@mail.com")).thenReturn(dbUser);
        when(passwordEncoder.verify("plainPassword", "hashedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken("test@mail.com")).thenReturn("Bearer token123");

        // Ejecución
        String token = authService.authenticate(inputUser);

        // Verificación
        assertEquals("Bearer token123", token);
    }

    @Test
    void authenticate_WrongPassword_ThrowsException() {
        Participant inputUser = new Participant();
        inputUser.setEmail("test@mail.com");
        inputUser.setPassword("wrongPassword");

        Participant dbUser = new Participant();
        dbUser.setPassword("hashedPassword");

        when(participantService.findByEmail("test@mail.com")).thenReturn(dbUser);
        when(passwordEncoder.verify("wrongPassword", "hashedPassword")).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> authService.authenticate(inputUser));
        assertEquals("Password incorrecto", ex.getMessage());
    }
}