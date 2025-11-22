package ar.edu.unnoba.poo2025.torneos.models; 
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue
    private Long idTournament;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=false)
    private Admin admin_id;

    // @ManyToOne
    // @JoinColumn(name="competition_id", nullable=false)
    // private Competition competition_id;                 //Como dijo facundo, se crea un deadlock tremendo aca. El huevo o la gallina


    @OneToMany(mappedBy = "tournament_id")
    private List<Competition> competition;

    
    private String name;
    private String descripction;
    private Date startDate;
    private Date endDate;
    private boolean published;


}
