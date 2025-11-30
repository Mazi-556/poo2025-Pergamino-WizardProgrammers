package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CreateParticipantRequestDTO {
    
    private String name;
    private String surname;
    @NotNull
    private Integer dni;
    private String email;
    private String password;
}