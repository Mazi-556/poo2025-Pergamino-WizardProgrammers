package ar.edu.unnoba.poo2025.torneos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@ExtendWith(MockitoExtension.class)
public class CompetitionServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private CompetitionServiceImp competitionService;

    @Test
    void testCrearCompetenciaExitosa() {
        Long tId = 1L;
        Tournament t = new Tournament();
        t.setIdTournament(tId);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.save(any(Competition.class))).thenAnswer(i -> i.getArguments()[0]);

        Competition result = competitionService.createCompetition(tId, "Nueva Competencia", 10, 100.0);

        assertNotNull(result);
        assertEquals("Nueva Competencia", result.getName());
        assertEquals(t, result.getTournament_id());
    }

    @Test
    void testErrorSiBorrarCompetenciaDeOtroTorneo() {
        Long tId = 1L;
        Tournament t1 = new Tournament();
        t1.setIdTournament(tId);

        Long otroTId = 2L;
        Tournament t2 = new Tournament();
        t2.setIdTournament(otroTId);

        Integer cId = 10;
        Competition c = new Competition();
        c.setIdCompetition(cId);
        c.setTournament_id(t2); 

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t1));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(c));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            competitionService.deleteCompetition(tId, cId);
        });

        assertEquals("La competencia no pertenece al torneo", ex.getMessage());
    }

    @Test
    void testActualizarCompetenciaExitosa() {
        Long tId = 1L;
        Integer cId = 10;
        Tournament t = new Tournament();
        t.setIdTournament(tId);

        Competition existingComp = new Competition();
        existingComp.setIdCompetition(cId);
        existingComp.setTournament_id(t);
        existingComp.setName("Nombre anterior");
        existingComp.setQuota(10);
        existingComp.setBasePrice(100.0);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(existingComp));
        when(competitionRepository.save(any(Competition.class))).thenAnswer(i -> i.getArguments()[0]);

        Competition result = competitionService.updateCompetition(tId, cId, "Nombre nuevo", 20, 200.0);

        assertEquals("Nombre nuevo", result.getName());
        assertEquals(20, result.getQuota());
        assertEquals(200.0, result.getBasePrice());
    }

    @Test
    void testErrorAlVerCompetenciasPublicasDeTorneoNoPublicado() {
        Long tId = 1L;
        Tournament t = new Tournament();
        t.setIdTournament(tId);
        t.setPublished(false);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));

        assertThrows(ar.edu.unnoba.poo2025.torneos.exceptions.TournamentNotReadyException.class, () -> {
            competitionService.getPublicCompetitions(tId);
        });
    }

    @Test
    void testCalculoDetalleCompetencia() {
        Long tId = 1L;
        Integer cId = 10;

        Tournament t = new Tournament();
        t.setIdTournament(tId);

        Competition c = new Competition();
        c.setIdCompetition(cId);
        c.setTournament_id(t);
        c.setName("Competencia 1");
        c.setQuota(50);
        c.setBasePrice(100.0);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(c));
        when(registrationRepository.countByCompetitionId(cId)).thenReturn(5L);
        when(registrationRepository.sumPriceByCompetitionId(cId)).thenReturn(500.0);

        AdminCompetitionDetailDTO detalle = competitionService.getCompetitionDetail(tId, cId);
        assertEquals(5L, detalle.getTotalRegistrations());
        assertEquals(500.0, detalle.getTotalRaised());
    }
}