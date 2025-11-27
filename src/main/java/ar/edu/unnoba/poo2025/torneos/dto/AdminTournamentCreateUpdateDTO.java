package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//se crea este DTO para no romper lo que ya esta hecho, se cran para el admin
//es lo que recibe post y put
public class AdminTournamentCreateUpdateDTO {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    
    public AdminTournamentCreateUpdateDTO() {}
}
