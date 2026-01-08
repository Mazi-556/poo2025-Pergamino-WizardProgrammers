package ar.edu.unnoba.poo2025.torneos.xdto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {
    private int registrationId;
    private int competitionId;
    private int participantId;
    private LocalDate date;
    private float price;  
}