package ar.edu.unnoba.poo2025.torneos.service;

import ar.edu.unnoba.poo2025.torneos.models.Participant;

public interface ParticipantService {
    void create(Participant participant);

    Participant findByEmail(String email);

    
}
