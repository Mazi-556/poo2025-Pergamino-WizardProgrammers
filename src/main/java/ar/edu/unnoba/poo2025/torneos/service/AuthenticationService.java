package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;

public interface AuthenticationService {
    String authenticate(Participant participant) throws Exception;
}
