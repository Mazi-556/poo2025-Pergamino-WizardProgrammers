package ar.edu.unnoba.poo2025.torneos.domain.ports.in;

import ar.edu.unnoba.poo2025.torneos.domain.model.Participant;

public interface CreateParticipantUseCase {

    void execute(Participant participant);

}
