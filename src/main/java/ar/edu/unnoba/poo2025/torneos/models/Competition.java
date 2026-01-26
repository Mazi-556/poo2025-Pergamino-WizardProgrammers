package ar.edu.unnoba.poo2025.torneos.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;

import java.util.List;

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
@Table(name = "competition")
public class Competition {

    @Id
    @GeneratedValue
    private int idCompetition;

    @ManyToOne
    @JoinColumn(name="tournament_id", nullable=false)
    private Tournament tournament_id;

    @OneToMany(mappedBy = "competition_id", cascade = CascadeType.REMOVE)   //Esto nuevo es para que al borrar una competencia se borren sus inscripciones
    private List<Registration> registrations;

    private String name;
    private double basePrice;
    private int quota;
}

