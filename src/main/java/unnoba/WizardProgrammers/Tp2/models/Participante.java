package unnoba.WizardProgrammers.Tp2.models;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Setter;
import lombok.Getter;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@Entity
public class Participante{

    private String nombre;
    private String apellido;
    private int DNI;
    private String email;
    private String contraseña;

    private void registrarseACompetencia() {
        //Lógica para registrarse a una competencia
    }
    private void inscribirseACompetencia() {
        //Lógica para inscribirse a una competencia
    }
}
