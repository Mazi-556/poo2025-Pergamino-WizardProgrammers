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
    // participante
    @Override
    public List<Tournament> getPublishedTournaments() {
        return tournamentRepository.findByPublishedTrue();
    }
    // participante
    @Override
    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
    //admin
    @Override
    public List<Tournament> getAllOrderByStartDateDesc() {
        return tournamentRepository.findAllByOrderByStartDateDesc();
    }
    //admin
    @Override
    public Tournament findById(Long id) throws Exception{
        return tournamentRepository.findById(id)
               .orElseThrow(() -> new Exception("Torneo no encontrado")); 
    }
    //admin
    @Override
    public void deleteTournament(Long id) throws Exception {
        if (!tournamentRepository.existsById(id)){
            throw new Exception("Torneo no encontrado");
        }
        tournamentRepository.deleteById(id);
    }
}
