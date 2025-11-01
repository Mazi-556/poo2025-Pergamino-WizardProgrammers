package unnoba.WizardProgrammers.models;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "administrador")
public class Administrador{
    
    @Id
    @GeneratedValue
    private int idAdministrador;    //Clave primaria

    //Relación uno a muchos con Torneo
    @OneToMany(mappedBy = "administrador_id")
    private List<Torneo> torneos;
    // Este es el atributo más importante para entender la relación.
    // ¿Qué hace?: Le dice a JPA: "Yo soy el lado 'uno' de la relación, 
    // pero no soy el dueño de ella. La responsabilidad de gestionar la clave foránea en 
    // la base de datos la tiene la clase Torneo, específicamente en el campo llamado administrador".

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

