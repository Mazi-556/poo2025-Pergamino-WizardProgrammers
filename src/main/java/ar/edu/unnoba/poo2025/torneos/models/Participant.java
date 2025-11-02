package ar.edu.unnoba.poo2025.torneos.models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.List;


import lombok.AllArgsConstructor;


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
    private int dni;
    
    @Column(unique = true)
    private String email;
    
    private String password;

    @OneToMany(mappedBy = "participante_id")
    private List<Registration> registrations;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return name;
    }

    public void setNombre(String name) {
        this.name = name;
    }

    public String getApellido() {
        return surname;
    }

    public void setApellido(String surname) {
        this.surname = surname;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}