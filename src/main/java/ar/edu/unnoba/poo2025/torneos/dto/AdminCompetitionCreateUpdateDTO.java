package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminCompetitionCreateUpdateDTO { //se utiliza para crear/actualizar una competencia dentro de un torneo
    private String name;
    private int quota;
    private double basePrice;  
}
