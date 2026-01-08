package ar.edu.unnoba.poo2025.torneos.xexceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}