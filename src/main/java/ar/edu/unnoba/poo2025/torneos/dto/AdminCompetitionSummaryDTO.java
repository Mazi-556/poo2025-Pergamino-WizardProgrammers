package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCompetitionSummaryDTO { // Resumen para listar competencias de un torneo

    private Integer id;
    private String name;
    private int quota;
    private double base_price;

    public AdminCompetitionSummaryDTO() {
    }

    public AdminCompetitionSummaryDTO(Integer id, String name, int quota, double base_price) {
        this.id = id;
        this.name = name;
        this.quota = quota;
        this.base_price = base_price;
    }
}
