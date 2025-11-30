package ar.edu.unnoba.poo2025.torneos.dto;
//se crea este DTO para no romper lo que ya esta hecho, se cran para el admin
//resumen para el listado
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminTournamentSummaryDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean published;

}
