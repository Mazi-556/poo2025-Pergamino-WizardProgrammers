package unnoba.WizardProgrammers.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Inscripcion {
    
    @Id
    @GeneratedValue
    private int idInscripcion;

    private Float precio;
    private Date fecha;


    @ManyToOne
    @JoinColumn(name="competencia_id", nullable=false)
    private Competencia competencia_id;
    @ManyToOne
    @JoinColumn(name="participante_id", nullable=false)
    private Participante participante_id;


}
