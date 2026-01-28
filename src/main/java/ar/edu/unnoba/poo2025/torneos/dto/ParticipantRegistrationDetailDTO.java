package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRegistrationDetailDTO {
    
    private Integer id;
    private double price;
    private LocalDate registrationDate;
    
    // Datos de la competencia
    private Integer competitionId;
    private String competitionName;
    
    // Datos del torneo
    private Long tournamentId;
    private String tournamentName;
    private String tournamentDescription;
    private LocalDateTime tournamentStartDate;
    private LocalDateTime tournamentEndDate;
}