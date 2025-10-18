package unnoba.WizardProgrammers.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Torneo {

    @Id
    @GeneratedValue
    private int idTorneo;

    @ManyToOne
    @JoinColumn(name="administrador_id", nullable=false)
    private Administrador administrador_id;

    @ManyToOne
    @JoinColumn(name="competencia_id", nullable=false)
    private Competencia competencia_id;


    @OneToMany(mappedBy = "torneo_id")
    private List<Competencia> competencias;

    
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;


}
