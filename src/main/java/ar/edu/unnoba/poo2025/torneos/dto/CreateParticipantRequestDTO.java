package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter //Creo que esto de getter y setter no hace falta si ya estan los metodos
@Setter
@NoArgsConstructor
public class CreateParticipantRequestDTO {
    
    private String name;
    private String surname;
    @NotNull
    private Integer dni;
    private String email;
    private String password;
}