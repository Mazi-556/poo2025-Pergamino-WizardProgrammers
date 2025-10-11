package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Torneo {

    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name="administrador_id", nullable=false)
    private Administrador administrador_id;
}
