package ar.edu.unnoba.poo2025.torneos.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

  
  public String getSubject(String tokenWithBearer) throws Exception {
    String token = stripBearer(tokenWithBearer);
    DecodedJWT jwt = JWT.decode(token);
    return jwt.getSubject();
  }

  private String stripBearer(String token) {
    if (token == null) return "";
    return token.startsWith("Bearer ") ? token.substring(7) : token;
  }
}