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
public class AdminCompetitionRegistrationDTO {

    private Integer id;
    
    private double finalPrice; 
    
    private LocalDate registrationDate;

    private Integer participantId;
    
    private String participantFirstName; 
    
    private String participantLastName;  
    
    private int participantDni;
    
    private String participantEmail; 
}