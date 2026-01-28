package ar.edu.unnoba.poo2025.torneos.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat; // <--- IMPORTANTE
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTournamentDetailDTO {
    private Long id;
    private String name;
    private String description;
  
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    
    private boolean published;
    private long totalRegistrations;
    private double totalRaised;
    
    private List<CompetitionSummaryDTO> competitions;
}