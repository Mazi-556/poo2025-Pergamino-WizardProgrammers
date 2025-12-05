package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.dto.RegistrationResponseDTO;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Registration;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionServiceImp;


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


   //Este metodo estaba en CompetitionServiceImp, pero me parecio mas apropiado moverlo aca, ya que el hecho de listar las inscripciones
   //corresponde mas al servicio de inscripciones que al de competencias.
    @Override
    public List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception {

        Tournament t = tournamentRepository.findById(tournamentId)  //TODO aca me dice que no se usa esta variable. Revisar
                .orElseThrow(() -> new Exception("Torneo no encontrado"));  //TODO hay que ver este tipo de exepciones
        
        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new Exception("Competencia no encontrada"));
                
        if (!c.getTournament_id().getIdTournament().equals(tournamentId)) {
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

        // Validar que la competencia sea del torneo indicado. Sin este if, un usuario podria entrar a una competencia de un torneo que no le corresponde
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
        
        //TODO Y si no tienen descuento? como era la logica de eso. O todos tienen descuento? Revisar

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
    
    
}