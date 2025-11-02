package ar.edu.unnoba.poo2025.torneos.models;
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
@Table(name = "admin")
public class Admin{
    
    @Id
    @GeneratedValue
    private int idAdmin;    //Clave primaria

    //Relación uno a muchos con Torneo
    @OneToMany(mappedBy = "admin_id")
    private List<Tournament> tournaments;
    // Este es el atributo más importante para entender la relación.
    // ¿Qué hace?: Le dice a JPA: "Yo soy el lado 'uno' de la relación, 
    // pero no soy el dueño de ella. La responsabilidad de gestionar la clave foránea en 
    // la base de datos la tiene la clase Torneo, específicamente en el campo llamado administrador".

    private String email;
    private String password;
    private String name;


    private void createTournament() {
        //Lógica para crear un torneo
    }
    private void createCompetition() {
        //Lógica para crear una competencia
    }

    private void editTournament() {
        //Lógica para editar un torneo
    }

    private void deleteTournament() {
        //Lógica para eliminar un torneo
    }

    private void createAdmin() {
        //Lógica para crear un administrador
    }
}

