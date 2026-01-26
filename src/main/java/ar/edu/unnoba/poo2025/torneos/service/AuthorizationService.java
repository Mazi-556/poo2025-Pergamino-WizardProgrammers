package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.models.Participant;

public interface AuthorizationService {
    Participant authorize(String token);
}
