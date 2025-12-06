package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParticipantRegistrationDTO {
    private int id; // ID Inscripción
    private LocalDate date;
    private float price;
    
    private Long tournamentId;
    private String tournamentName;

    private Integer competitionId;
    private String competitionName;
}