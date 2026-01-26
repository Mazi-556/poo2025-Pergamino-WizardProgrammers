package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
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
public class RegistrationServiceIml implements RegistrationService {
    private final CompetitionService competitionService;  
    private final RegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;

    public RegistrationServiceIml(CompetitionService competitionService, RegistrationRepository registrationRepository, TournamentRepository tournamentRepository, CompetitionRepository competitionRepository) {
        this.competitionService = competitionService;
        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
    }

    @Override
    public List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) {
        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));
        
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
                        p.getDni()
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

        if (t.getStartDate().isBefore(LocalDate.now())) {
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
        registration.setDate(LocalDate.now());
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
            r.getCompetition_id().getName()
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

        return new ParticipantRegistrationDetailDTO(
                r.getIdregistration(),
                r.getPrice(),
                r.getDate(),
                c.getIdCompetition(),
                c.getName(),
                t.getIdTournament(),
                t.getName(),
                t.getDescription(),
                t.getStartDate(),
                t.getEndDate()
        );
    }
}