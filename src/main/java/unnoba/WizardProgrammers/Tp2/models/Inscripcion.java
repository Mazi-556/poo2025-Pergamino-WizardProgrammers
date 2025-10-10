package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class Inscripcion {
    
    private Float precio;
    private Date fecha;
}
