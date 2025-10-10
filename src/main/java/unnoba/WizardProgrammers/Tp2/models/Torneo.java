package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class Torneo {

    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;
}
