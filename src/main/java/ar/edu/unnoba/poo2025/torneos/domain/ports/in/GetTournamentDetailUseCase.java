package ar.edu.unnoba.poo2025.torneos.domain.ports.in;

import ar.edu.unnoba.poo2025.torneos.infrastructure.adapter.in.web.rest.dto.AdminTournamentDetailDTO;

public interface GetTournamentDetailUseCase {
    AdminTournamentDetailDTO execute(Long id) throws Exception;
}