package ar.edu.unnoba.poo2025.torneos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importamos los repos y DTOs que faltaban
import ar.edu.unnoba.poo2025.torneos.Repository.CompetitionRepository; 
import ar.edu.unnoba.poo2025.torneos.Repository.RegistrationRepository;
import ar.edu.unnoba.poo2025.torneos.Repository.TournamentRepository;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentCreateUpdateDTO;
import ar.edu.unnoba.poo2025.torneos.dto.AdminTournamentDetailDTO;
import ar.edu.unnoba.poo2025.torneos.dto.CompetitionSummaryDTO;
import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException;
import ar.edu.unnoba.poo2025.torneos.exceptions.ResourceNotFoundException;
import ar.edu.unnoba.poo2025.torneos.models.Tournament;

@Service
public class TournamentServiceImp implements TournamentService {
    
    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;
    private final CompetitionRepository competitionRepository; // Nuevo campo

    // Constructor actualizado con los 3 repositorios
    public TournamentServiceImp(TournamentRepository tournamentRepository, 
                                RegistrationRepository registrationRepository,
                                CompetitionRepository competitionRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
        this.competitionRepository = competitionRepository;
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
        Tournament t = findById(id); 
        
        long totalRegistrations = registrationRepository.countByTournamentId(id);
        
        Double totalAmount = registrationRepository.sumPriceByTournamentId(id);
        double finalAmount = (totalAmount != null) ? totalAmount : 0.0;

        // FIX: Buscamos las competencias, calculamos sus inscriptos y armamos la lista
        List<CompetitionSummaryDTO> competitions = competitionRepository.findByTournamentId(id).stream()
            .map(c -> {
                long regs = registrationRepository.countByCompetitionId(c.getIdCompetition());
                return new CompetitionSummaryDTO(
                    c.getIdCompetition(),
                    c.getName(),
                    c.getQuota(),
                    c.getBasePrice(),
                    regs
                );
            }).collect(Collectors.toList());

        // Ahora si llamamos al constructor con los 9 argumentos (la lista al final)
        return new AdminTournamentDetailDTO(
            t.getIdTournament(),
            t.getName(),
            t.getDescription(),
            t.getStartDate(),
            t.getEndDate(),
            t.isPublished(),
            totalRegistrations,
            finalAmount,
            competitions 
        );
    }
    
    @Override
    @Transactional
    public void publish(Long id) { 
        Tournament t = this.findById(id); 
        
        if (t.getEndDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se puede publicar un torneo que ya finalizó");
        }
        
        t.setPublished(true);
        tournamentRepository.save(t);
    }    

    @Override
    @Transactional
    public Tournament updateTournament(Long id, AdminTournamentCreateUpdateDTO dto) { 
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
            if (dto.getEndDate().isBefore(t.getStartDate())) {
                throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
            }
            t.setEndDate(dto.getEndDate());
        }
        
        return tournamentRepository.save(t);
    }
}