package ar.edu.unnoba.poo2025.torneos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;   
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentServiceImp tournamentService;

    @Test
    void testRetornaTorneosPublicados() {
        Tournament t = new Tournament();
        t.setName("Torneo Oficial");
        t.setPublished(true);

        when(tournamentRepository.findByPublishedTrueAndEndDateAfter(any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(t));
        List<Tournament> resultado = tournamentService.getPublishedTournaments();

        assertEquals(1, resultado.size());
        assertEquals("Torneo Oficial", resultado.get(0).getName());
    }

    //Este test hace que el servicio lance una excepcion y asegura que buscar una id que exista
    @Test
    void testSiTorneoNoExiste() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            tournamentService.findById(99L);
        });
    }

    //Test para validar el proceso de creación de un nuevo torneo
    @Test
    void testGuardarTorneo() {
        Tournament t = new Tournament();
        t.setName("Nuevo Torneo");
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(t);
        Tournament guardado = tournamentService.saveTournament(t);
        assertNotNull(guardado);
        verify(tournamentRepository).save(t);
    }

    //Test para lanzar una excepcion si la fecha fin es anterior al inicio 
    @Test
    void testAlActualizar() {
        Long id = 1L;
        Tournament tExistente = new Tournament();
        tExistente.setStartDate(LocalDateTime.now().plusDays(5));
        AdminTournamentCreateUpdateDTO dtoInvalido = new AdminTournamentCreateUpdateDTO();
        dtoInvalido.setEndDate(LocalDateTime.now().plusDays(2)); 
        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tExistente));
        assertThrows(BadRequestException.class, () -> {
            tournamentService.updateTournament(id, dtoInvalido);
        });
    }

    //Test para lanzar una excepcion si se intenta borrar un torneo que no existe
    @Test
    void testTorneoInexistente() {
        Long idInexistente = 99L;
        when(tournamentRepository.existsById(idInexistente)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            tournamentService.deleteTournament(idInexistente);
        });
    }
}