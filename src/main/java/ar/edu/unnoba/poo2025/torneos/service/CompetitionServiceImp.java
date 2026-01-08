package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;
import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;
import ar.edu.unnoba.poo2025.torneos.domain.model.Registration;
import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionDetailDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminCompetitionRegistrationDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.CompetitionSummaryDTO;

@Service
public class CompetitionServiceImp implements CompetitionService {
    private final TournamentRepository tournamentRepository;
    private final CompetitionRepository competitionRepository;
    private final RegistrationRepository registrationRepository;


    public CompetitionServiceImp(TournamentRepository tournamentRepository, CompetitionRepository competitionRepository, RegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.competitionRepository = competitionRepository;
        this.registrationRepository = registrationRepository;
    }
    
    private Tournament getTournamentOrThrow(Long tournamentId) throws Exception {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new Exception("Torneo no encontrado"));
    }
    
    private Competition getCompetitionOrThrow(Integer competitionId) throws Exception {
        return competitionRepository.findById(competitionId)
                .orElseThrow(() -> new Exception("Competencia no encontrada"));
    }
    
    
    @Override
    public List<Competition> findByTournamentId(Long tournamentId) throws Exception {
        getTournamentOrThrow(tournamentId);
        return competitionRepository.findByTournamentId(tournamentId);
    }
    
    
    @Override
    public Competition createCompetition(Long tournamentId, String name, int quota, double basePrice) throws Exception {
        
        Tournament t = getTournamentOrThrow(tournamentId);

        Competition c = new Competition();
        c.setTournament_id(t);
        c.setName(name);
        c.setQuota(quota);
        c.setBasePrice(basePrice);

        return competitionRepository.save(c);
    }

    @Override
    public Competition updateCompetition(Long tournamentId, Integer competitionId, String name, int quota, double basePrice) throws Exception {
        
        Tournament t = getTournamentOrThrow(tournamentId);
        Competition c = getCompetitionOrThrow(competitionId);

        if (c.getTournament_id() == null ||
            !c.getTournament_id().getIdTournament().equals(t.getIdTournament())) {
            throw new Exception("La competencia no pertenece al torneo");
        }

        c.setName(name);
        c.setQuota(quota);
        c.setBasePrice(basePrice);

        return competitionRepository.save(c);
    }
    
    
    @Override
    public void deleteCompetition(Long tournamentId, Integer competitionId) throws Exception {
        
        Tournament t = getTournamentOrThrow(tournamentId);
        Competition c = getCompetitionOrThrow(competitionId);

        if (c.getTournament_id() == null ||
            !c.getTournament_id().getIdTournament().equals(t.getIdTournament())) {
            throw new Exception("La competencia no pertenece al torneo");
        }

        competitionRepository.delete(c);
    }

    
    @Override
    public Competition findByIdAndTournament(Long tournamentId,
                                             Integer competitionId) throws Exception {
        Tournament t = getTournamentOrThrow(tournamentId);
        Competition c = getCompetitionOrThrow(competitionId);

        if (c.getTournament_id() == null ||
            !c.getTournament_id().getIdTournament().equals(t.getIdTournament())) {
            throw new Exception("La competencia no pertenece al torneo");
        }

        return c;
    }


    @Override
    public List<Registration> findRegistrationsByCompetition(Long tournamentId,
                                                             Integer competitionId) throws Exception {
        Competition c = findByIdAndTournament(tournamentId, competitionId);
        return registrationRepository.findByCompetitionId(c.getIdCompetition());
    }

    
    @Override
    public AdminCompetitionDetailDTO getCompetitionDetail(Long tournamentId, Integer competitionId) throws Exception {
        Competition c = findByIdAndTournament(tournamentId, competitionId);

        long totalRegistrations = registrationRepository.countByCompetitionId(competitionId);
        double totalAmount = registrationRepository.sumPriceByCompetitionId(competitionId);

        return new AdminCompetitionDetailDTO(
                c.getIdCompetition(),
                c.getName(),
                c.getQuota(),
                c.getBasePrice(),
                totalRegistrations,
                totalAmount
        );
    }
    
    
    @Override
    public List<CompetitionSummaryDTO> getCompetitionSummaries(Long tournamentId) throws Exception {

        getTournamentOrThrow(tournamentId); 
        
        List<Competition> list = competitionRepository.findByTournamentId(tournamentId);

        return list.stream()
                .map(c -> new CompetitionSummaryDTO(
                        c.getIdCompetition(),
                        c.getName(),
                        c.getQuota(),
                        c.getBasePrice()
                ))
                .collect(Collectors.toList());
    }



    @Override
    public List<CompetitionSummaryDTO> getPublicCompetitions(Long tournamentId) throws Exception {

        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new Exception("Torneo no encontrado"));


            if (!t.isPublished()) {
            throw new Exception("El torneo no se encuentra disponible.");
        }

        List<Competition> list = competitionRepository.findByTournamentId(tournamentId);
        
        return list.stream()
                .map(c -> new CompetitionSummaryDTO(
                        c.getIdCompetition(),
                        c.getName(),
                        c.getQuota(),
                        c.getBasePrice()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public CompetitionSummaryDTO getPublicCompetitionDetail(Long tournamentId, Integer competitionId) throws Exception {

        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new Exception("Torneo no encontrado"));

        if (!t.isPublished()) {
            throw new Exception("El torneo no se encuentra disponible.");
        }

        Competition c = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new Exception("Competencia no encontrada"));

        if (!c.getTournament_id().getIdTournament().equals(tournamentId)) {
            throw new Exception("La competencia no pertenece al torneo indicado.");
        }

        return new CompetitionSummaryDTO(
                c.getIdCompetition(),
                c.getName(),
                c.getQuota(),
                c.getBasePrice()
        );
    }
}
