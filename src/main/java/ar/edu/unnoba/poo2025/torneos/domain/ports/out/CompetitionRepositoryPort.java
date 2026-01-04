package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;

public interface CompetitionRepositoryPort {

    List<Competition> findByTournamentId(Long tournamentId);

    void delete(Competition c);

    Competition save(Competition c);//TODO Mepa que aca va mejor competition en lugar de "c"

    Competition findById(int competitionId);

}
