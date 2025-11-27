package ar.edu.unnoba.poo2025.torneos.dto;


import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminCompetitionRegistrationDTO { //el admin reciba la lista de inscripciones de una competencia

    private Integer id;
    private double base_price;
    private LocalDate date;

    private Integer participantId;
    private String participantName;
    private String participantSurname;
    private int participantDni;

    public AdminCompetitionRegistrationDTO() {
    }

    public AdminCompetitionRegistrationDTO(Integer id, double base_price, LocalDate date, Integer participantId, String participantName, String participantSurname, int participantDni) {
        this.id = id;
        this.base_price = base_price;
        this.date = date;
        this.participantId = participantId;
        this.participantName = participantName;
        this.participantSurname = participantSurname;
        this.participantDni = participantDni;
    }
}
