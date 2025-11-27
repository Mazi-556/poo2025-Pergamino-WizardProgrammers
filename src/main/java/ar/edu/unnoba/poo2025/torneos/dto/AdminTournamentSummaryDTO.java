package ar.edu.unnoba.poo2025.torneos.dto;
//se crea este DTO para no romper lo que ya esta hecho, se cran para el admin
//resumen para el listado
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminTournamentSummaryDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean published;

    public AdminTournamentSummaryDTO(Long id, String name, LocalDate startDate, LocalDate endDate, boolean published) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.published = published;
    }
    public AdminTournamentSummaryDTO() {}
}
