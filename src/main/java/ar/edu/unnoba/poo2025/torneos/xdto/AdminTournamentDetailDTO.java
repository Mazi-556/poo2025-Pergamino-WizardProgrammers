package ar.edu.unnoba.poo2025.torneos.xdto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//se crea este DTO para no romper lo que ya esta hecho, se cran para el admin
//es un dto donde se encuentra el detalle completyo de los torneos
public class AdminTournamentDetailDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean published;
    private long totalRegistrations;
    private double totalAmount;

}
