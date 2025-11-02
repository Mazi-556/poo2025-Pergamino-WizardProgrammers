package ar.edu.unnoba.poo2025.torneos.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import ar.edu.unnoba.poo2025.torneos.models.Registration;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "competencia")
public class Competition {

    @Id
    @GeneratedValue
    private int idCompetition;

    @ManyToOne
    @JoinColumn(name="torneo_id", nullable=false)
    private Tournament tournament_id;

    @OneToMany(mappedBy = "competencia_id")
    private List<Registration> registrations;

    private String name;
    private double base_price;
    private int quota; //cupo


    




}

