package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminAccountResponseDTO {
    private int id;
    private String email;

    public AdminAccountResponseDTO(int id, String email) {
        this.id = id;
        this.email = email;
    }
}
