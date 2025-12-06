package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

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
    public Tournament findById(Long id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado con id: " + id)); 
    }
    
    
    //admin
    @Override
        public void deleteTournament(Long id) { // Quitamos "throws Exception"
            if (!tournamentRepository.existsById(id)){
                // CORRECCIÓN: Usamos la excepción específica
                throw new ResourceNotFoundException("Torneo no encontrado con id: " + id);
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
        // 1. Buscamos el torneo (reutilizamos tu método findById que ya lanza excepción si no existe)
        Tournament t = this.findById(id); 
        
        // 2. Aplicamos la lógica de negocio (cambiar estado)
        t.setPublished(true);
        
        // 3. Guardamos los cambios
        tournamentRepository.save(t);
    }    



    @Override
    public Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto) throws Exception {
        // 1. Buscamos el torneo (reutilizamos tu método que ya lanza excepción)
        Tournament t = this.findById(id);

        // 2. Lógica de "Patch" / Actualización Parcial
        // Solo actualizamos si el dato nuevo NO es nulo
        if (dto.getName() != null) {
            t.setName(dto.getName());
        }
        
        // Cuidado con el typo 'descripction' si aún no lo arreglaste en la clase Tournament
        if (dto.getDescription() != null) {
            t.setDescription(dto.getDescription()); 
        }
        
        if (dto.getStartDate() != null) {
            t.setStartDate(dto.getStartDate());
        }
        
        if (dto.getEndDate() != null) {
            t.setEndDate(dto.getEndDate());
        }
        
        // Nota: 'published' no lo tocamos aquí para evitar que se ponga en false accidentalmente.
        // Para publicar/despublicar ya tienes el método específico 'publish'.

        // 3. Guardamos y retornamos
        return tournamentRepository.save(t);
    }
}
