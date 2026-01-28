package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat; // <--- IMPORTANTE
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTournamentCreateUpdateDTO {
    private String name;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
    private LocalDateTime startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
}