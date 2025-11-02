package ar.edu.unnoba.poo2025.torneos.Util;

import com.password4j.Password;

public class PasswordEncoder {

    // Método para codificar (hashear) una contraseña en texto plano
    public String encode(String rawPassword) {
        return Password.hash(rawPassword)
                       .withBcrypt()
                       .getResult(); // Devuelve el hash resultante
    }

    // Método para verificar si el hash corresponde a la contraseña original
    public boolean verify(String rawPassword, String encodedPassword) {
        return Password.check(rawPassword, encodedPassword)
                       .withBcrypt(); // Devuelve true si coincide
    }
}