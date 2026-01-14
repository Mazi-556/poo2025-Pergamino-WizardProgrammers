package ar.edu.unnoba.poo2025.torneos.infrastructure.config;

import ar.edu.unnoba.poo2025.torneos.application.usecase.CreateParticipantService;
import ar.edu.unnoba.poo2025.torneos.application.usecase.GetTournamentDetailService;
import ar.edu.unnoba.poo2025.torneos.application.usecase.PublishTournamentService;

import ar.edu.unnoba.poo2025.torneos.domain.ports.out.ParticipantRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.RegistrationRepositoryPort;
import ar.edu.unnoba.poo2025.torneos.domain.ports.out.TournamentRepositoryPort;

import ar.edu.unnoba.poo2025.torneos.Util.PasswordEncoder;









import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class BeanConfiguration {

    @Bean 
    public CreateParticipantService createParticipantService(
            ParticipantRepositoryPort participantRepositoryPort, 
            PasswordEncoder passwordEncoder) {
        
        return new CreateParticipantService(participantRepositoryPort, passwordEncoder);
    }

    @Bean
    public PublishTournamentService publishTournamentService(
            TournamentRepositoryPort repository) {
        return new PublishTournamentService(repository);
    }

    @Bean
    public GetTournamentDetailService getTournamentDetailService(
        TournamentRepositoryPort tournamentRepository, 
        RegistrationRepositoryPort registrationRepository) {
        return new GetTournamentDetailService(tournamentRepository, registrationRepository);
    }
}