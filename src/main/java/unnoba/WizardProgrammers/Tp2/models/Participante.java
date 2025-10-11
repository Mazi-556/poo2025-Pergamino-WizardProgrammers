package unnoba.WizardProgrammers.Tp2.models;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participante{

    @Id
    @GeneratedValue
    private int idParticipante;

    private String nombre;
    private String apellido;
    private int DNI;
    private String email;
    private String contraseña;

    @OneToMany(mappedBy = "participante_id")
    private List<Inscripcion> inscripciones;



    private void registrarseACompetencia() {
        //Lógica para registrarse a una competencia
    }
    private void inscribirseACompetencia() {
        //Lógica para inscribirse a una competencia
    }
}
