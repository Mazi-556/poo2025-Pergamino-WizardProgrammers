package ar.edu.unnoba.poo2025.torneos.dto;

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
//es lo que recibe post y put
public class AdminTournamentCreateUpdateDTO {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    
}
