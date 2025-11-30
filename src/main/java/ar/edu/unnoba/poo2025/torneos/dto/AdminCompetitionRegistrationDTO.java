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
public class AdminCompetitionRegistrationDTO { //el admin reciba la lista de inscripciones de una competencia

    private Integer id;
    private double base_price;
    private LocalDate date;

    private Integer participantId;
    private String participantName;
    private String participantSurname;
    private int participantDni;

}
