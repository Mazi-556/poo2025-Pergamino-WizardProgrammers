package ar.edu.unnoba.poo2025.torneos.domain.ports.out;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;

public interface ParticipantRepositoryPort {
    Participant save(Participant participant);
    Participant findByEmail(String email);
    Participant findByDni(int dni);
}