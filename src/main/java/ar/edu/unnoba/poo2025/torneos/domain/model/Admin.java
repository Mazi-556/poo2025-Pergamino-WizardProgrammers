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
public class Admin{
    private int idAdmin;

    private List<Tournament> tournaments;

    private String email;

    private String password;

    private String name;
}

