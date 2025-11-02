package ar.edu.unnoba.poo2025.torneos.dto;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class CreateParticipantRequestDTO {
    
    private String name;
    private String surname;
    @JsonProperty("dni")// Esto sirve para que con Postman al mandar el post, solo acepte "dni" en minusculas, ya que en mayusculas provoca errores.
    private int dni;
    private String email;
    private String password;

    public CreateParticipantRequestDTO() {
    }

/*     public String getName() {            Segun vi, la libreria Lombok ya hace esto de los getters y setters
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } */
}