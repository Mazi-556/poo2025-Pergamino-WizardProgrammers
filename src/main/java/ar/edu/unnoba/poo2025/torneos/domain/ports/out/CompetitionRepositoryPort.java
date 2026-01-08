package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;

public interface CompetitionRepositoryPort {

    List<Competition> findByTournamentId(Long tournamentId);

    void delete(Competition competition);

    Competition save(Competition competition);

    Competition findById(int competitionId);

}
