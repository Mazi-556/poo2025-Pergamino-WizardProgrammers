package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import java.time.LocalDate;
import java.util.List;

public interface TournamentRepositoryPort {
    List<Tournament> findActiveTournaments(LocalDate date);

    boolean exists(Long id);

    void delete(Long id);

    Tournament findById(Long id);

    Tournament save(Tournament tournament);
}