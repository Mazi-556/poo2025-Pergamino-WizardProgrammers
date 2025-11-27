package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public AdminTournamentDetailDTO(Long id, String name, String description, LocalDate startDate, LocalDate endDate, boolean published, long totalRegistrations, double totalAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.published = published;
        this.totalRegistrations = totalRegistrations;
        this.totalAmount = totalAmount;
    }
    public AdminTournamentDetailDTO() {}
}
