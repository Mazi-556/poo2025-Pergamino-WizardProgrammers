package unnoba.WizardProgrammers.Tp2.models;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
public abstract class Usuario {
    private String nombre;
    private String apellido;
    private int DNI;
    private String email;
    private String password;
    

    private void ingresarEmail(String email) {
        this.email = email;
    }

    private void ingresarPassword(String password) {
        this.password = password;
    }
}
