package ar.edu.unnoba.poo2025.torneos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unnoba.poo2025.torneos.Repository.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.models.Participant;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ParticipantServiceImp participantService;

    @Test
    void testCrearParticipanteExitoso() {
        Participant nuevo = new Participant();
        nuevo.setEmail("maximo@gmail.com");
        nuevo.setDni(12345678);
        nuevo.setPassword("123456");

        when(participantRepository.findByEmail("maximo@gmail.com")).thenReturn(null);
        when(participantRepository.findByDNI(12345678)).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("@@@@@@@");

        participantService.create(nuevo);
        assertEquals("@@@@@@@", nuevo.getPassword());
        verify(participantRepository).save(nuevo);
    }

    @Test
    void testCrearFallaPorEmailDuplicado() {
        Participant nuevo = new Participant();
        nuevo.setEmail("existe@gmail.com");
        nuevo.setDni(65468864);

        when(participantRepository.findByEmail("existe@gmail.com")).thenReturn(new Participant());

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> {
            participantService.create(nuevo);
        });
        assertEquals("Ya existe un participante con ese email.", ex.getMessage());
        verify(participantRepository, never()).save(any());
    }

    @Test
    void testCrearFallaPorDniDuplicado() {
        Participant nuevo = new Participant();
        nuevo.setEmail("unico@gmail.com");
        nuevo.setDni(846168138);

        when(participantRepository.findByEmail("unico@gmail.com")).thenReturn(null);
        when(participantRepository.findByDNI(846168138)).thenReturn(new Participant());

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> {
            participantService.create(nuevo);
        });
        assertEquals("Ya existe un participante con ese DNI.", ex.getMessage());
        verify(participantRepository, never()).save(any());
    }

    @Test
    void testBuscarPorEmailEncontrado() {
        String email = "dario@gmail.com";
        Participant mockP = new Participant();
        mockP.setEmail(email);
        mockP.setName("Dario");

        when(participantRepository.findByEmail(email)).thenReturn(mockP);

        Participant resultado = participantService.findByEmail(email);

        assertNotNull(resultado);
        assertEquals("Dario", resultado.getName());
    }

    @Test
    void testBuscarPorEmailNoEncontrado() {
        String email = "noexiste@gmail.com";
        when(participantRepository.findByEmail(email)).thenReturn(null);

        Participant resultado = participantService.findByEmail(email);
        
        assertEquals(null, resultado);
    }
}