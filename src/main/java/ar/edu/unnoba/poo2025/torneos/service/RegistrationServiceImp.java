package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.time.LocalDateTime; // IMPORTANTE: Necesario
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Registration;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;

// Imports de excepciones
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.TournamentNotReadyException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceAlreadyExistsException;
import ar.edu.unnoba.poo2025.torneos.exceptions.UnauthorizedException;

@Service
public class RegistrationServiceImp implements RegistrationService { 
    private final RegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;

    public RegistrationServiceImp(RegistrationRepository registrationRepository, TournamentRepository tournamentRepository, CompetitionRepository competitionRepository) {
        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
    }

    @Override
    public List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) {

        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competencia no encontrada"));
                
        if (!c.getTournament_id().getIdTournament().equals(tournamentId)) {
             throw new BadRequestException("La competencia no pertenece al torneo.");
        }

        List<Registration> registrations = registrationRepository.findByCompetitionIdWithParticipant(c.getIdCompetition());

        return registrations.stream()
                .map(r -> {
                    Participant p = r.getParticipant_id();
                    return new AdminCompetitionRegistrationDTO(
                        r.getIdregistration(),
                        r.getPrice(), 
                        r.getDate(),  
                        p.getIdParticipant(),
                        p.getName(),    
                        p.getSurname(), 
                        p.getDni(),
                        p.getEmail()   
                    );
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public RegistrationResponseDTO registerParticipant(Long tournamentId, Integer competitionId, Participant participant) {
        
        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));
        
        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competencia no encontrada"));

        if (!c.getTournament_id().getIdTournament().equals(tournamentId)) {
            throw new BadRequestException("La competencia no pertenece al torneo indicado.");
        }

        if (!t.isPublished()) {
            throw new TournamentNotReadyException("No puedes inscribirte a un torneo no publicado.");
        }

        // CORREGIDO: Usamos LocalDateTime.now() para comparar con t.getStartDate() (que es LocalDateTime)
        if (t.getStartDate().isBefore(LocalDateTime.now())) {
            throw new TournamentNotReadyException("El torneo ya ha comenzado o finalizado. No se permiten inscripciones.");
        }

        long currentRegistrations = registrationRepository.countByCompetitionId(competitionId);
        if (currentRegistrations >= c.getQuota()) {
            throw new BadRequestException("No hay cupo disponible en esta competencia.");
        }

        if (registrationRepository.existsByParticipantAndCompetition(participant.getIdParticipant(), competitionId)) {
            throw new ResourceAlreadyExistsException("Ya estás inscripto en esta competencia.");
        }

        double price = c.getBasePrice();
        long otherRegistrationsInTournament = registrationRepository.countByParticipantAndTournament(participant.getIdParticipant(), tournamentId);
        
        if (otherRegistrationsInTournament > 0) {
            price = price * 0.5; 
        }

        Registration registration = new Registration();
        registration.setCompetition_id(c);
        registration.setParticipant_id(participant);
        registration.setDate(LocalDate.now()); // Fecha de inscripción sigue siendo LocalDate, está bien
        registration.setPrice((float) price);

        Registration saved = registrationRepository.save(registration);

        return new RegistrationResponseDTO(
                saved.getIdregistration(),
                c.getIdCompetition(),
                participant.getIdParticipant(),
                saved.getDate(),
                saved.getPrice()
        );
    }
    
    @Override
    public List<ParticipantRegistrationDTO> getParticipantRegistrations(Integer participantId) {
        List<Registration> list = registrationRepository.findByParticipantId(participantId);
        
        return list.stream().map(r -> new ParticipantRegistrationDTO(
            r.getIdregistration(),
            r.getDate(),
            r.getPrice(),
            r.getCompetition_id().getTournament_id().getIdTournament(),
            r.getCompetition_id().getTournament_id().getName(),
            r.getCompetition_id().getIdCompetition(),
            r.getCompetition_id().getName(),
            r.getCompetition_id().getQuota()
        )).collect(Collectors.toList());
    }

    @Override
    public ParticipantRegistrationDetailDTO getRegistrationDetail(Integer registrationId, Integer participantId) {
        
        Registration r = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));

        if (r.getParticipant_id().getIdParticipant() != participantId) {
            throw new UnauthorizedException("No tienes permiso para ver esta inscripción.");
        }

        Competition c = r.getCompetition_id();
        Tournament t = c.getTournament_id();

        // CORREGIDO: Asegurate que ParticipantRegistrationDetailDTO.java tenga los campos
        // tournamentStartDate y tournamentEndDate como LocalDateTime.
        return new ParticipantRegistrationDetailDTO(
                r.getIdregistration(),
                (double) r.getPrice(), // Casting explícito por si acaso
                r.getDate(),
                c.getIdCompetition(), // competitionId? Revisa tu DTO, a veces es String competitionName
                // IMPORTANTE: Este constructor depende 100% de tu archivo DTO. 
                // Si el DTO pide Strings, poné c.getName(). Si pide IDs, poné c.getId().
                // Basado en tu código anterior, parece que espera:
                // id, price, date, compId, compName, tournId, tournName, tournDesc, start, end
                c.getName(),
                t.getIdTournament(), // Ojo, tu DTO en el mensaje anterior tenia otra estructura. 
                // Voy a asumir la estructura que usaste en tu código pegado:
                t.getName(),
                t.getDescription(),
                t.getStartDate(), // LocalDateTime
                t.getEndDate()    // LocalDateTime
        );
    }
}