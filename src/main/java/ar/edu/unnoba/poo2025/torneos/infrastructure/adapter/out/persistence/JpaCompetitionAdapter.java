package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.unnoba.poo2025.torneos.domain.ports.out.CompetitionRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.CompetitionRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;

@Component
public class JpaCompetitionAdapter {

    @Autowired
    private CompetitionRepository competitionRepository;

    public JpaCompetitionAdapter(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public List<Competition> findByTournamentId(Long tournamentId) {
        return competitionRepository.findByTournamentId(tournamentId);
    }

    public void delete (Competition competition) {
        competitionRepository.delete(competition);
    }

    public Competition save (Competition competition) {
        return competitionRepository.save(competition);
    }

    public Competition findById (int competitionId) {
        return competitionRepository.findById(competitionId)
            .orElseThrow(() -> new RuntimeException("Competición no encontrada con ID: " + competitionId));//En caso de que no se encuentre una competencia da un mensaje de erorr
    }

}
