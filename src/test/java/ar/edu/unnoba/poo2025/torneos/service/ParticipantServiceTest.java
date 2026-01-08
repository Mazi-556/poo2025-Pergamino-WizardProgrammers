package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.ParticipantRepository;
import ar.edu.unnoba.poo2025.torneos.xexceptions.ResourceAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ParticipantServiceImp participantService;

    @Test
    void create_ShouldSaveParticipant_WhenDataIsValid() {
        Participant p = new Participant();
        p.setEmail("nuevo@mail.com");
        p.setDni(12345678);
        p.setPassword("1234");

        // Simulamos que NO existe nadie con ese email ni DNI
        when(participantRepository.findByEmail("nuevo@mail.com")).thenReturn(null);
        when(participantRepository.findByDNI(12345678)).thenReturn(null);
        when(passwordEncoder.encode("1234")).thenReturn("hashed_1234");

        participantService.create(p);

        // Verificamos que se llamó a guardar y que la contraseña se hasheó
        verify(participantRepository).save(p);
        assertEquals("hashed_1234", p.getPassword());
    }

    @Test
    void create_ShouldThrowException_WhenEmailExists() {
        Participant p = new Participant();
        p.setEmail("existente@mail.com");

        // Simulamos que YA existe alguien
        when(participantRepository.findByEmail("existente@mail.com")).thenReturn(new Participant());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            participantService.create(p);
        });

        // Aseguramos que NUNCA se intente guardar si falló la validación
        verify(participantRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenDniExists() {
        Participant p = new Participant();
        p.setEmail("nuevo@mail.com");
        p.setDni(99999999);

        // Email bien, pero DNI repetido
        when(participantRepository.findByEmail("nuevo@mail.com")).thenReturn(null);
        when(participantRepository.findByDNI(99999999)).thenReturn(new Participant());

        Exception e = assertThrows(ResourceAlreadyExistsException.class, () -> {
            participantService.create(p);
        });
        
        assertEquals("Ya existe un participante con ese DNI.", e.getMessage());
    }
}