package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;
import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionServiceImp;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.ParticipantRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.ParticipantRegistrationDetailDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.RegistrationResponseDTO;


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
    public List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception {

        Tournament t = tournamentRepository.findById(tournamentId)  
                .orElseThrow(() -> new Exception("Torneo no encontrado"));
        
        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new Exception("Competencia no encontrada"));
                
        if (!c.getTournament_id().getIdTournament().equals(t.getIdTournament())) {
             throw new Exception("La competencia no pertenece al torneo.");
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
    public RegistrationResponseDTO registerParticipant(Long tournamentId, Integer competitionId, Participant participant) throws Exception {
        
        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new Exception("Torneo no encontrado"));
        
        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new Exception("Competencia no encontrada"));

        if (!c.getTournament_id().getIdTournament().equals(tournamentId)) {
            throw new Exception("La competencia no pertenece al torneo indicado.");
        }

        if (!t.isPublished()) {
            throw new Exception("No puedes inscribirte a un torneo no publicado.");
        }

        if (t.getStartDate().isBefore(LocalDate.now())) {
            throw new Exception("El torneo ya ha comenzado o finalizado. No se permiten inscripciones.");
        }

        long currentRegistrations = registrationRepository.countByCompetitionId(competitionId);
        if (currentRegistrations >= c.getQuota()) {
            throw new Exception("No hay cupo disponible en esta competencia.");
        }

        if (registrationRepository.existsByParticipantAndCompetition(participant.getIdParticipant(), competitionId)) {
            throw new Exception("Ya estás inscripto en esta competencia.");
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
        registration.setPrice((float) price);   //TODO Aca hay una inconsistencia de tipos de datos. Un poco mas arriba declaramos undouble price y aca float.
                                                //Esto lleva a perder un poco de precision. Pero funciona igual, solo que en sistemas grandes es muy malo

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
public ParticipantRegistrationDetailDTO getRegistrationDetail(Integer registrationId, Integer participantId) throws Exception {
    
    Registration r = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new Exception("Inscripción no encontrada"));

    if (r.getParticipant_id().getIdParticipant() != participantId) {
        throw new Exception("No tienes permiso para ver esta inscripción.");
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