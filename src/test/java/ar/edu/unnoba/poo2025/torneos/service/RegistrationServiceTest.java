package ar.edu.unnoba.poo2025.torneos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import ar.edu.unnoba.poo2025.torneos.models.*;
import ar.edu.unnoba.poo2025.torneos.Repository.*;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.TournamentNotReadyException;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private RegistrationServiceImp registrationService;


    //Que hace este test: Asegurar que el programa impida correctamente la inscripción de un participante si ya no hay lugares disponibles en la competencia.
    @Test
    void testCupoLleno() {
        Long tId = 1L;
        Integer cId = 10;
        
        Tournament t = new Tournament();
        t.setIdTournament(tId);
        t.setPublished(true);
        t.setStartDate(LocalDateTime.now().plusDays(1)); 
        
        Competition c = new Competition();
        c.setIdCompetition(cId);
        c.setQuota(2); 
        c.setTournament_id(t);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(c));
        when(registrationRepository.countByCompetitionId(cId)).thenReturn(2L);

        assertThrows(BadRequestException.class, () -> {
            registrationService.registerParticipant(tId, cId, new Participant());
        });
    }

    //Se valida que si un participante ya registra una inscripción en el mismo torneo, 
    //el sistema divide automáticamente el precio base de la competencia a la mitad antes de guardarlo.
    @Test
    void testAplicarDescuento50() {
        Long tournamentId = 1L;
        Integer competitionId = 10;
        
        Participant participant = new Participant();
        participant.setIdParticipant(100);

        Tournament t = new Tournament();
        t.setIdTournament(tournamentId);
        t.setPublished(true);
        t.setStartDate(LocalDateTime.now().plusDays(2));

        Competition c = new Competition();
        c.setIdCompetition(competitionId);
        c.setQuota(10);
        c.setBasePrice(1000.0);
        c.setTournament_id(t);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(c));
        when(registrationRepository.countByCompetitionId(competitionId)).thenReturn(0L);
        when(registrationRepository.existsByParticipantAndCompetition(100, competitionId)).thenReturn(false);
        when(registrationRepository.countByParticipantAndTournament(100, tournamentId)).thenReturn(1L);
        when(registrationRepository.save(any(Registration.class))).thenAnswer(i -> i.getArguments()[0]);

        RegistrationResponseDTO respuesta = registrationService.registerParticipant(tournamentId, competitionId, participant);
        assertEquals(500.0, respuesta.getPrice(), "El precio debería tener un 50% de descuento");
        verify(registrationRepository).save(argThat(reg -> reg.getPrice() == 500.0f));
    }

    //No debe permitir inscripciones si el torneo ya comenzó.
    @Test
    void testNoDeberiaPermitirInscripcionSiElTorneoYaComenzo() { 

        Long tId = 1L;
        Integer cId = 10;
    
        Tournament t = new Tournament();
        t.setIdTournament(tId);
        t.setPublished(true);
        t.setStartDate(LocalDateTime.now().minusHours(1)); 
    
        Competition c = new Competition();
        c.setTournament_id(t);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(c));

        TournamentNotReadyException exception = assertThrows(TournamentNotReadyException.class, () -> {
            registrationService.registerParticipant(tId, cId, new Participant());
        });

        assertEquals("El torneo ya ha comenzado o finalizado. No se permiten inscripciones.", exception.getMessage());
    }

    //test del denominado camino feliz, donde se dan todas las condiciones para registrar un participante
    @Test
    void deberiaRegistrarParticipanteExitosamente() {
        Long tId = 1L;
        Integer cId = 10;
        Participant p = new Participant();
        p.setIdParticipant(100);

        Tournament t = new Tournament();
        t.setIdTournament(tId);
        t.setPublished(true);
        t.setStartDate(LocalDateTime.now().plusDays(5));

        Competition c = new Competition();
        c.setIdCompetition(cId);
        c.setQuota(20);
        c.setBasePrice(1500.0);
        c.setTournament_id(t);

        when(tournamentRepository.findById(tId)).thenReturn(Optional.of(t));
        when(competitionRepository.findById(cId)).thenReturn(Optional.of(c));
        when(registrationRepository.countByCompetitionId(cId)).thenReturn(5L);
        when(registrationRepository.existsByParticipantAndCompetition(100, cId)).thenReturn(false);
        when(registrationRepository.countByParticipantAndTournament(100, tId)).thenReturn(0L);

        Registration regSalvada = new Registration();
        regSalvada.setIdregistration(500);
        regSalvada.setPrice(1500.0f);
        regSalvada.setDate(LocalDate.now());
        when(registrationRepository.save(any(Registration.class))).thenReturn(regSalvada);

        RegistrationResponseDTO resultado = registrationService.registerParticipant(tId, cId, p);

        assertNotNull(resultado);
        assertEquals(1500.0, resultado.getPrice(), "El precio debe ser el base sin descuentos");
        assertEquals(500, resultado.getIdRegistration());
        
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
}