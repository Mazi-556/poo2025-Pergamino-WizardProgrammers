package unnoba.WizardProgrammers.Tp2.models;
import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Administrador{
    
    private String email;
    private String password;
    private String nombre;

    private void crearTorneo() {
        //Lógica para crear un torneo
    }
    private void crearCompetencia() {
        //Lógica para crear una competencia
    }

    private void editarTorneo() {
        //Lógica para editar un torneo
    }

    private void eliminarTorneo() {
        //Lógica para eliminar un torneo
    }

    private void crearAdministrador() {
        //Lógica para crear un administrador
    }
}

