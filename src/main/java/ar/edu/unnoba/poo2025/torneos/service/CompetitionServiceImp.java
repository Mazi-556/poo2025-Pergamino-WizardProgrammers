package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.models.Competition;
import ar.edu.unnoba.poo2025.torneos.models.Registration;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

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
        getTournamentOrThrow(tournamentId); // valida existencia
        return competitionRepository.findByTournamentId(tournamentId);
    }
    @Override
    public Competition createCompetition(Long tournamentId, String name, int quota, double basePrice) throws Exception {
        
        Tournament t = getTournamentOrThrow(tournamentId);

        Competition c = new Competition();
        c.setTournament_id(t);
        c.setName(name);
        c.setQuota(quota);
        c.setBase_price(basePrice);

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
        c.setBase_price(basePrice);

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
}
