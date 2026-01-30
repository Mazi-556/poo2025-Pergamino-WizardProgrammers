package ar.edu.unnoba.poo2025.torneos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unnoba.poo2025.torneos.Util.JwtTokenUtil;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private AuthorizationServiceImp authorizationService;

    @Test
    void testAutorizacionExitosaConBearer() {
        String tokenRaw = "Bearer token.valido.seguro";
        String tokenClean = "token.valido.seguro";
        String email = "facundo@gmail.com";

        Participant participant = new Participant();
        participant.setEmail(email);
        participant.setName("Facundo");

        when(jwtTokenUtil.getSubject(tokenClean)).thenReturn(email);
        when(participantService.findByEmail(email)).thenReturn(participant);

        Participant resultado = authorizationService.authorize(tokenRaw);
        assertNotNull(resultado);
        assertEquals("Facundo", resultado.getName());
        verify(jwtTokenUtil).validateToken(tokenClean); 
    }

    @Test
    void testAutorizacionExitosaSinBearer() {
        String tokenRaw = "token.valido.sin.prefix";
        String email = "dario@gmail.com";

        Participant participant = new Participant();
        participant.setEmail(email);

        when(jwtTokenUtil.getSubject(tokenRaw)).thenReturn(email);
        when(participantService.findByEmail(email)).thenReturn(participant);

        Participant resultado = authorizationService.authorize(tokenRaw);

        assertNotNull(resultado);
        verify(jwtTokenUtil).validateToken(tokenRaw);
    }

    @Test
    void testTokenNuloOVacio() {
        UnauthorizedException exNull = assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authorize(null);
        });
        assertEquals("Token de autorización ausente.", exNull.getMessage());

        UnauthorizedException exEmpty = assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authorize("");
        });
        assertEquals("Token de autorización ausente.", exEmpty.getMessage());
    }

    @Test
    void testTokenInvalido() {
        String tokenMalo = "Bearer token.falso";
        
        doThrow(new UnauthorizedException("Token expirado o inválido"))
            .when(jwtTokenUtil).validateToken("token.falso");

        assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authorize(tokenMalo);
        });
    }

    @Test
    void testUsuarioNoEncontrado() {
        String token = "Bearer token.valido";
        String emailFantasma = "falso@test.com";

        when(jwtTokenUtil.getSubject("token.valido")).thenReturn(emailFantasma);
        when(participantService.findByEmail(emailFantasma)).thenReturn(null);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> {
            authorizationService.authorize(token);
        });

        assertEquals("Usuario no encontrado o no autorizado.", ex.getMessage());
    }
}