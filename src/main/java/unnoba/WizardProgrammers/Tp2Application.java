package unnoba.WizardProgrammers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import unnoba.WizardProgrammers.Util.PasswordEncoder;

@SpringBootApplication
public class Tp2Application {

    public static void main(String[] args) {
        // Esta linea inicia la app
        SpringApplication.run(Tp2Application.class, args); 
    }
        @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }
}