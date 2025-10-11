package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;


}
