package ar.edu.unnoba.poo2025.torneos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.out.persistence.jpa.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.xdto.AdminTournamentDetailDTO;

import java.time.LocalDate;

@Service
public class TournamentServiceImp implements TournamentService  {
    
    @Autowired
    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    public TournamentServiceImp(TournamentRepository tournamentRepository, RegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }
    // participante
    @Override
    public List<Tournament> getPublishedTournaments() {
        return tournamentRepository.findByPublishedTrueAndEndDateAfter(LocalDate.now());
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
    
    

    @Override
    public void deleteTournament(Long id) throws Exception { //TODO: Exception
        if (!tournamentRepository.existsById(id)){
            throw new Exception("Torneo no encontrado");
        }
        tournamentRepository.deleteById(id);
    }

    @Override
    public AdminTournamentDetailDTO getTournamentDetail(Long id) throws Exception { //TODO: Exception
        Tournament t = findById(id); 
        
        // En lugar de recorrer todas las listas de inscripciones, le pedimos a la base de datos que nos haga el conteo y la suma
        //Gracias a esto nos ahorramos hacer 2 for anidados. Una verdadero crimen de guerra
        long totalRegistrations = registrationRepository.countByTournamentId(id);
        double totalAmount = registrationRepository.sumPriceByTournamentId(id);

        return new AdminTournamentDetailDTO(
            t.getIdTournament(),
            t.getName(),
            t.getDescription(),
            t.getStartDate(),
            t.getEndDate(),
            t.isPublished(),
            totalRegistrations,
            totalAmount
        );
    }
    
    
    @Override
    public void publish(Long id) throws Exception {
        Tournament t = this.findById(id); 
        
        t.setPublished(true);
        
        tournamentRepository.save(t);
    }    



    @Override
    public Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto) throws Exception {

        Tournament t = this.findById(id);

        if (dto.getName() != null) {
            t.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            t.setDescription(dto.getDescription()); 
        }
        
        if (dto.getStartDate() != null) {
            t.setStartDate(dto.getStartDate());
        }
        
        if (dto.getEndDate() != null) {
            t.setEndDate(dto.getEndDate());
        }
        
        //TODO probar si da error al intentar cambiar el atributo published desde aqui (con el metodo update tournament)
        return tournamentRepository.save(t);
    }
}
