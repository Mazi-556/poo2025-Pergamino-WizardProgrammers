package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@Entity
public class Competencias {

    private String nombre;
    private double precio_base;
    private int cupo;


    

    @ManyToOne
    @JoinColumn(name="torneo_id", nullable=false)
    private Torneo torneo_id;


}

