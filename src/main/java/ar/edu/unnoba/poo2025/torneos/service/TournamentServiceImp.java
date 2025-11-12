package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@Service
public class TournamentServiceImp implements TournamentService  {
    
    @Autowired
    private TournamentRepository tournamentRepository;

    public TournamentServiceImp(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    @Override
    public List<Tournament> getPublishedTournaments() {
        return tournamentRepository.findByPublishedTrue();
    }
    @Override
    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
}
