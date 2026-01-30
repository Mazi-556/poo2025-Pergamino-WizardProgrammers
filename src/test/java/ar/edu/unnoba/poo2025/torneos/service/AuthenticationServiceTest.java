package ar.edu.unnoba.poo2025.torneos.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private ParticipantService participantService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationServiceImp authService;


    @Test
    void testAutenticarExitosamente() {
        Participant incoming = new Participant();
        incoming.setEmail("alumno@unnoba.edu.ar");
        incoming.setPassword("password123");

        Participant dbUser = new Participant();
        dbUser.setEmail("alumno@unnoba.edu.ar");
        dbUser.setPassword("encoded_password");

        when(participantService.findByEmail("alumno@unnoba.edu.ar")).thenReturn(dbUser);
        when(passwordEncoder.verify("password123", "encoded_password")).thenReturn(true);
        when(jwtTokenUtil.generateToken("alumno@unnoba.edu.ar")).thenReturn("fake-jwt-token");

        String token = authService.authenticate(incoming);

        assertNotNull(token);
        assertEquals("fake-jwt-token", token);
        verify(jwtTokenUtil).generateToken("alumno@unnoba.edu.ar");
    }


    @Test
    void testSiElUsuarioNoExiste() {
        Participant incoming = new Participant();
        incoming.setEmail("no-existe@test.com");
        
        when(participantService.findByEmail("no-existe@test.com")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            authService.authenticate(incoming);
        });
    }

    @Test
    void testSiLaContrasenaEsIncorrecta() {
        Participant incoming = new Participant();
        incoming.setEmail("user@test.com");
        incoming.setPassword("incorrecta");

        Participant dbUser = new Participant();
        dbUser.setPassword("hash_correcto");

        when(participantService.findByEmail("user@test.com")).thenReturn(dbUser);
        when(passwordEncoder.verify("incorrecta", "hash_correcto")).thenReturn(false);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> {
            authService.authenticate(incoming);
        });
        
        assertEquals("Credenciales inválidas", ex.getMessage());
    }

    @Test
    void noDeberiaGenerarTokenSiElPasswordFalla() {
        // Arrange
        Participant incoming = new Participant();
        incoming.setEmail("test@test.com");
        incoming.setPassword("clave_erronea");

        Participant db = new Participant();
        db.setPassword("hash_real");

        when(participantService.findByEmail("test@test.com")).thenReturn(db);
        when(passwordEncoder.verify("clave_erronea", "hash_real")).thenReturn(false);

        // Act
        assertThrows(UnauthorizedException.class, () -> authService.authenticate(incoming));

        // Assert: Verificamos que NUNCA se llamó al generador de token
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }

    @Test
    void deberiaLanzarExcepcionSiElEmailEsNulo() {
        // Arrange
        Participant incoming = new Participant();
        incoming.setEmail(null);

        // Act & Assert
        assertThrows(Exception.class, () -> authService.authenticate(incoming));
    }
}