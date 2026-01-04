package ar.edu.unnoba.poo2025.torneos.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant{
    private int idParticipant;

    private String name;

    private String surname;

    private Integer dni;
    
    private String email;
    
    private String password;

    private List<Registration> registrations;

}