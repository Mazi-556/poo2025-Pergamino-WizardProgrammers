package unnoba.WizardProgrammers.dto;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class CreateParticipanteRequestDTO {
    
    private String nombre;
    private String apellido;
    @JsonProperty("dni")// Esto sirve para que con Postman al mandar el post, solo acepte "dni" en minusculas, ya que en mayusculas provoca errores.
    private int dni;
    private String email;
    private String contraseña;

    public CreateParticipanteRequestDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}