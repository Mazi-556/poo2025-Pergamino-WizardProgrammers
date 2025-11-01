package unnoba.WizardProgrammers.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "competencia")
public class Competencia {

    @Id
    @GeneratedValue
    private int idCompetencia;

    @ManyToOne
    @JoinColumn(name="torneo_id", nullable=false)
    private Torneo torneo_id;

    @OneToMany(mappedBy = "competencia_id")
    private List<Inscripcion> inscripciones;

    private String nombre;
    private double precio_base;
    private int cupo;


    




}

