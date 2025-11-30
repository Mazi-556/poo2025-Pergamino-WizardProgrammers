package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TournamentResponseDTO {
    private Long id;
    private String name;
    private String description;
}
