package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.unnoba.poo2025.torneos.dto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Participant;
import ar.edu.unnoba.poo2025.torneos.models.Registration;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.service.CompetitionServiceImp;


public class RegistrationServiceIml implements RegistrationService {
    private final CompetitionService competitionService;    //TODO ver porque es final
    private final RegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;


    //TODO revisar lo de @Autowired. Cual usar
    public RegistrationServiceIml(CompetitionService competitionService, RegistrationRepository registrationRepository, TournamentRepository tournamentRepository, CompetitionRepository competitionRepository) {
        this.competitionService = competitionService;
        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
    }

    
   //Esto estaba en CompetitionServiceImp, pero me parecio mas apropiado moverlo aca, ya que el hecho de listar las inscripciones
   //corresponde mas al servicio de inscripciones que al de competencias.
    @Override
    public List<AdminCompetitionRegistrationDTO> getCompetitionRegistrations(Long tournamentId, Integer competitionId) throws Exception {

        Tournament t = tournamentRepository.findById(tournamentId)
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
    
}
