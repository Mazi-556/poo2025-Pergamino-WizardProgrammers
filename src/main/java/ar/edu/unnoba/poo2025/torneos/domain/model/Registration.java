package ar.edu.unnoba.poo2025.torneos.domain.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    private int idregistration;

    private Float price;

    private LocalDate date;

    private Competition competition_id;
    
    private Participant participant_id;


}
