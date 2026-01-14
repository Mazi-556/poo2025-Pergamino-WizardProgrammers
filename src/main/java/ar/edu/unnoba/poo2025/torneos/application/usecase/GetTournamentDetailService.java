package ar.edu.unnoba.poo2025.torneos.application.usecase;

import ar.edu.unnoba.poo2025.torneos.domain.model.Tournament;
import ar.edu.unnoba.poo2025.torneos.domain.ports.in.GetTournamentDetailUseCase;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.RegistrationRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.TournamentRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.in.web.rest.dto.AdminTournamentDetailDTO;

public class GetTournamentDetailService implements GetTournamentDetailUseCase {

    private final TournamentRepositoryPort tournamentRepository;
    private final RegistrationRepositoryPort registrationRepository;

    public GetTournamentDetailService(TournamentRepositoryPort tournamentRepository, 
                                      RegistrationRepositoryPort registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public AdminTournamentDetailDTO execute(Long id) {
        
        Tournament tournament = tournamentRepository.findById(id);
        if (tournament == null) {
            throw new RuntimeException("Torneo no encontrado con ID: " + id);
        }

        //Contamos inscripciones y monto total
        long totalRegistrations = registrationRepository.countByTournamentId(id);
        Double totalAmount = registrationRepository.sumPriceByTournamentId(id);

        //DTO
        return new AdminTournamentDetailDTO(
            tournament.getIdTournament(),
            tournament.getName(),
            tournament.getDescription(),
            tournament.getStartDate(),
            tournament.getEndDate(),
            tournament.isPublished(),
            totalRegistrations,
            totalAmount
        );
    }
}