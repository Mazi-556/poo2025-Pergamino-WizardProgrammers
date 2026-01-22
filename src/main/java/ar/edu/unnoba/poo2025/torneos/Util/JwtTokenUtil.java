package ar.edu.unnoba.poo2025.torneos.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import ar.edu.unnoba.poo2025.torneos.exceptions.BadRequestException; // Importamos tu excepción

@Component
public class JwtTokenUtil {

  private static final String SECRET = "super-secret-key-change-me";
  private static final Algorithm ALG = Algorithm.HMAC512(SECRET);

  public String generateToken(String subject) {
    String token = JWT.create()
        .withSubject(subject)
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(10, ChronoUnit.DAYS))
        .sign(ALG);
    return "Bearer " + token;
  }

  public boolean verify(String tokenWithBearer) {
    try {
      String token = stripBearer(tokenWithBearer);
      JWTVerifier verifier = JWT.require(ALG).build();
      verifier.verify(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // Eliminamos el 'throws Exception' para que no ensucie los controladores
  public String getSubject(String tokenWithBearer) {
    try {
        String token = stripBearer(tokenWithBearer);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    } catch (Exception e) {
        throw new BadRequestException("No se pudo obtener el sujeto del token");
    }
  }

  private String stripBearer(String token) {
    if (token == null) return "";
    return token.startsWith("Bearer ") ? token.substring(7) : token;
  }

  /**
   * Versión limpia de validación.
   * Lanza BadRequestException (Unchecked) para que el GlobalExceptionHandler la capture solo.
   */
  public void validateToken(String token) {
    if (!verify(token)) {
        // Al lanzar BadRequestException, el IDE dejará de marcar error en rojo en tus Resources
        throw new BadRequestException("Token inválido o expirado");
    }
  }
}