package ar.edu.unnoba.poo2025.torneos.models; 

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id; // <-- AGREGA ESTA LÍNEA
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue
    @Column(name = "id_tournament")
    private Long idTournament;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=true) 
    private Admin admin_id;

    @OneToMany(mappedBy = "tournament_id")
    private List<Competition> competition;
    
    private String name;
    private String descripction; // Mantenemos tu variable tal cual está en el JSON
    
    private LocalDate startDate; // <-- CAMBIADO A LocalDate
    private LocalDate endDate;   // <-- CAMBIADO A LocalDate
    
    private boolean published;
}