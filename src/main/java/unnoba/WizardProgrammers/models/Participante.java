package unnoba.WizardProgrammers.models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;

import java.util.List;


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
    public String getPassword() {
        return contraseña;
    }
    public void setPassword(String contraseña) {
        this.contraseña = contraseña;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
