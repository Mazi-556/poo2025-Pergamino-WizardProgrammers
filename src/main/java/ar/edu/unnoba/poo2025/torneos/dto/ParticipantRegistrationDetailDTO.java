package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRegistrationDetailDTO {
    private int registrationId;
    private float price;
    private LocalDate date;

    private Integer competitionId;
    private String competitionName;

    private Long tournamentId;
    private String tournamentName;
    private String tournamentDescription;
    private LocalDate tournamentStartDate;
    private LocalDate tournamentEndDate;
}