package unnoba.WizardProgrammers.resource;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unnoba.WizardProgrammers.dto.CreateParticipanteRequestDTO;
import unnoba.WizardProgrammers.models.Participante;
import unnoba.WizardProgrammers.service.ParticipanteService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;


@RestController
@RequestMapping("/participant")
public class ParticipanteResource {
    @Autowired
    private ParticipanteService participanteService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateParticipanteRequestDTO dto) {
    try {
        Participante p = new Participante();
        p.setEmail(dto.getEmail());
        p.setPassword(dto.getPassword());

        participanteService.create(p);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Participante creado correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    }
}
