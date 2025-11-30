package ar.edu.unnoba.poo2025.torneos.models;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participant")
public class Participant{

    @Id
    @GeneratedValue
    private int idParticipant;

    private String name;
    private String surname;

    @Column(name = "dni", unique = true)
    @NotNull (message= "El DNI es obligatorio")
    private Integer dni;
    
    @Column(unique = true)
    private String email;
    
    private String password;

    @OneToMany(mappedBy = "participant_id")
    private List<Registration> registrations;

}