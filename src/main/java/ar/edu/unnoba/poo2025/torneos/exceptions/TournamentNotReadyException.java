package ar.edu.unnoba.poo2025.torneos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// spring devuelve un 403 Forbidden automáticamente
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TournamentNotReadyException extends RuntimeException {
    public TournamentNotReadyException(String message) {
        super(message);
    }
}