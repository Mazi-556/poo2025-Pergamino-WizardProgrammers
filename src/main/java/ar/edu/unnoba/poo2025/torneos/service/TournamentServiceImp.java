package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@Service
public class TournamentServiceImp implements TournamentService {
    
    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    // Quitamos @Autowired en el campo y usamos solo el constructor (mejor practica)
    public TournamentServiceImp(TournamentRepository tournamentRepository, RegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public List<Tournament> getPublishedTournaments() {
        return tournamentRepository.findByPublishedTrueAndEndDateAfter(LocalDate.now());
    }

    @Override
    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
    
    @Override
    public List<Tournament> getAllOrderByStartDateDesc() {
        return tournamentRepository.findAllByOrderByStartDateDesc();
    }

    @Override
    public Tournament findById(Long id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado con id: " + id)); 
    }
    
    @Override
    @Transactional
    public void deleteTournament(Long id) { 
        if (!tournamentRepository.existsById(id)){
            throw new ResourceNotFoundException("No se puede eliminar: Torneo no encontrado con id: " + id);
        }
        tournamentRepository.deleteById(id);
    }

    @Override
    public AdminTournamentDetailDTO getTournamentDetail(Long id) { 
        Tournament t = findById(id); // Reutiliza findById que ya lanza la excepcion
        
        long totalRegistrations = registrationRepository.countByTournamentId(id);
        
        // Manejamos el caso donde la suma sea nula si no hay inscripciones
        Double totalAmount = registrationRepository.sumPriceByTournamentId(id);
        double finalAmount = (totalAmount != null) ? totalAmount : 0.0;

        return new AdminTournamentDetailDTO(
            t.getIdTournament(),
            t.getName(),
            t.getDescription(),
            t.getStartDate(),
            t.getEndDate(),
            t.isPublished(),
            totalRegistrations,
            finalAmount
        );
    }
    
    @Override
    @Transactional
    public void publish(Long id) { 
        Tournament t = this.findById(id); 
        
        // Validación de negocio extra: No publicar si ya paso la fecha de fin
        if (t.getEndDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se puede publicar un torneo que ya finalizó");
        }
        
        t.setPublished(true);
        tournamentRepository.save(t);
    }    

    @Override
    @Transactional
    public Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto) { // LIMPIO: Sin throws Exception
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
            // Validacion: Fecha fin no puede ser antes que inicio
            if (dto.getEndDate().isBefore(t.getStartDate())) {
                throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
            }
            t.setEndDate(dto.getEndDate());
        }
        
        return tournamentRepository.save(t);
    }
}