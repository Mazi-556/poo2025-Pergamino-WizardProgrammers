package ar.edu.unnoba.poo2025.torneos.domain.model; 

import java.time.LocalDate;
import java.util.List;

import ar.edu.unnoba.poo2025.torneos.domain.model.Admin;
import ar.edu.unnoba.poo2025.torneos.domain.model.Competition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    private Long idTournament;

    private Admin admin_id;

    private List<Competition> competitions;
    
    private String name;

    private String description; 
    
    private LocalDate startDate; 

    private LocalDate endDate;   
    
    private boolean published;
}