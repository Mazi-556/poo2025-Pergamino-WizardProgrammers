package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.TournamentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    
    @Mock
    private RegistrationRepository registrationRepository; // Necesario porque el Service lo usa en el constructor

    @InjectMocks
    private TournamentServiceImp tournamentService;

    @Test
    void findById_ShouldReturnTournament_WhenExists() throws Exception {
        Tournament t = new Tournament();
        t.setIdTournament(1L);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(t));

        Tournament result = tournamentService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdTournament());
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        Exception e = assertThrows(Exception.class, () -> tournamentService.findById(99L));
        assertEquals("Torneo no encontrado", e.getMessage());
    }

    @Test
    void publish_ShouldChangeStatusToTrue() throws Exception {
        Tournament t = new Tournament();
        t.setPublished(false);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(t));
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(i -> i.getArgument(0));

        tournamentService.publish(1L);

        assertTrue(t.isPublished(), "El torneo debería haber cambiado a publicado=true");
        verify(tournamentRepository).save(t);
    }
}