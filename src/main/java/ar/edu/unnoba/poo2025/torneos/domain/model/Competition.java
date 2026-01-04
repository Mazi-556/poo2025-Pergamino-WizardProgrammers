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
public class Competition {

    private int idCompetition;

    private Tournament tournament_id;

    private List<Registration> registrations;

    private String name;

    private double basePrice;

    private int quota;
}

