package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTournamentDetailDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean published;
    private long totalRegistrations;
    private double totalRaised; 
    
    private List<CompetitionSummaryDTO> competitions;
}