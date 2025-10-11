package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@Entity
public class Inscripcion {
    
    private Float precio;
    private Date fecha;


    @ManyToOne
    @JoinColumn(name="competencia_id", nullable=false)
    private Competencias competencia_id;
    @ManyToOne
    @JoinColumn(name="participante_id", nullable=false)
    private Participante participante_id;


}
