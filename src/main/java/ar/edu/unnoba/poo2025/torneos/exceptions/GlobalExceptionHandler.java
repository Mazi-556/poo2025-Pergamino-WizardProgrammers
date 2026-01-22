package ar.edu.unnoba.poo2025.torneos.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Para ResourceNotFoundException (devuelve 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // 2. Para BadRequestException (errores de lógica)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // 3. Para ResourceAlreadyExistsException
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        // Normalmente para "ya existe" se usa el código 409 Conflict
        return ResponseEntity.status(HttpStatus.CONFLICT) 
                .body(Map.of("error", ex.getMessage()));
    }

    // 4. Para TournamentNotReadyException 
    @ExceptionHandler(TournamentNotReadyException.class)
    public ResponseEntity<?> handleNotReady(TournamentNotReadyException ex) {
        // Podés usar 400 Bad Request o 412 Precondition Failed
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(Map.of("error", ex.getMessage()));
    }

    // 5. El error generico (Si se captura una excepcion que no está manejada específicamente, cae aca y devuelve error 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error inesperado en el servidor"));
    }
}
