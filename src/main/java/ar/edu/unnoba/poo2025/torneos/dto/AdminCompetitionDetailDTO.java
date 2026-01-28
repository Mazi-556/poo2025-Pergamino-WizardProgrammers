package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCompetitionDetailDTO { 

    private int id;
    private String name;
    private int quota;
    
    //precio base, sin descuentos
    private double basePrice; 
    
    //cantidad de inscriptos
    private long totalRegistrations;
   
    //monto total recaudado en la competencia
    private double totalRaised; 
}