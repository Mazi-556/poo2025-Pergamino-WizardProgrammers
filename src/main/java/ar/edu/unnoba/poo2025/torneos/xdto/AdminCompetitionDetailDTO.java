package ar.edu.unnoba.poo2025.torneos.xdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminCompetitionDetailDTO { //detalle/resumen de una competencia de torneo

    private int id;
    private String name;
    private int quota;
    private double base_price;
    private long totalRegistrations;
    private double totalAmount;
    //TO DO: Juntar los dtos similares
}
