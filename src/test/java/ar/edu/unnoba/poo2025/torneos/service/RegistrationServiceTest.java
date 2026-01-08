package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;
import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.xdto.RegistrationResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private CompetitionService competitionService; // Inyectado en el constructor aunque no se use en este método

    @InjectMocks
    private RegistrationServiceIml registrationService;

    private Tournament tournament;
    private Competition competition;
    private Participant participant;

    @BeforeEach
    void setUp() {
        // Datos de prueba comunes
        tournament = new Tournament();
        tournament.setIdTournament(1L);
        tournament.setPublished(true);
        // Fecha futura para que no falle la validación de "ya comenzó"
        tournament.setStartDate(LocalDate.now().plusDays(10)); 

        competition = new Competition();
        competition.setIdCompetition(1);
        competition.setTournament_id(tournament);
        competition.setQuota(10);
        competition.setBasePrice(100.0);

        participant = new Participant();
        participant.setIdParticipant(1);
    }

    @Test
    void registerParticipant_Success_NoDiscount() throws Exception {
        // 1. Configurar Mocks
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));
        when(registrationRepository.countByCompetitionId(1)).thenReturn(5L); // Hay cupo (5 < 10)
        when(registrationRepository.existsByParticipantAndCompetition(1, 1)).thenReturn(false);
        // No tiene otras inscripciones, por tanto no hay descuento
        when(registrationRepository.countByParticipantAndTournament(1, 1L)).thenReturn(0L);
        
        when(registrationRepository.save(any(Registration.class))).thenAnswer(invocation -> {
            Registration r = invocation.getArgument(0);
            r.setIdregistration(99); // Simular ID generado
            return r;
        });

        // 2. Ejecutar
        RegistrationResponseDTO result = registrationService.registerParticipant(1L, 1, participant);

        // 3. Verificar
        assertNotNull(result);
        assertEquals(100.0f, result.getPrice(), "El precio debería ser el base (100.0)");
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void registerParticipant_Success_WithDiscount() throws Exception {
        // Simular que YA tiene una inscripción en este torneo
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));
        when(registrationRepository.countByParticipantAndTournament(1, 1L)).thenReturn(1L); 
        
        when(registrationRepository.save(any(Registration.class))).thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        RegistrationResponseDTO result = registrationService.registerParticipant(1L, 1, participant);

        // Verificar descuento del 50%
        assertEquals(50.0f, result.getPrice(), "El precio debería tener 50% de descuento (50.0)");
    }

    @Test
    void registerParticipant_ThrowsException_WhenNoQuota() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));
        // Simular cupo lleno (10 >= 10)
        when(registrationRepository.countByCompetitionId(1)).thenReturn(10L); 

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.registerParticipant(1L, 1, participant);
        });

        assertEquals("No hay cupo disponible en esta competencia.", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void registerParticipant_ThrowsException_WhenTournamentNotPublished() {
        tournament.setPublished(false); // Torneo no publicado
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));

        assertThrows(Exception.class, () -> 
            registrationService.registerParticipant(1L, 1, participant)
        );
    }
}