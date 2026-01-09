package ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence;

import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.TournamentRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class JpaTournamentAdapter implements TournamentRepositoryPort {

    private final TournamentRepository tournamentRepository;

    public JpaTournamentAdapter(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public List<Tournament> findActiveTournaments(LocalDate date) {
        return tournamentRepository.findByPublishedTrueAndEndDateAfter(date);
    }

    @Override
    public boolean exists(Long id) {
        return tournamentRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        tournamentRepository.deleteById(id);
    }

    @Override
    public Tournament findById(Long id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
}