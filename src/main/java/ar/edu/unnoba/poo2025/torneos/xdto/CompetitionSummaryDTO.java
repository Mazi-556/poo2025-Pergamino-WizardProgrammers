package ar.edu.unnoba.poo2025.torneos.xdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionSummaryDTO { // Resumen para listar competencias de un torneo

    private Integer id;
    private String name;
    private int quota;
    private double basePrice;

}
