package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCompetitionCreateUpdateDTO { //se utiliza para crear/actualizar una competencia dentro de un torneo
    private String name;
    private int quota;
    private double base_price;

    public AdminCompetitionCreateUpdateDTO() {
    }
}
