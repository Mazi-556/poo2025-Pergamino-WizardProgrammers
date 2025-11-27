package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCompetitionDetailDTO { //detalle/resumen de una competencia de torneo

    private int id;
    private String name;
    private int quota;
    private double base_price;
    private long totalRegistrations;
    private double totalAmount;


    public AdminCompetitionDetailDTO() {
    }


    public AdminCompetitionDetailDTO(int id, String name, int quota, double base_price, long totalRegistrations, double totalAmount) {
        this.id = id;
        this.name = name;
        this.quota = quota;
        this.base_price = base_price;
        this.totalRegistrations = totalRegistrations;
        this.totalAmount = totalAmount;
    }
}
