package ar.edu.unnoba.poo2025.torneos.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registration")
public class Registration {
    
    @Id
    @GeneratedValue
    private int idregistration;

    private Float price;
    private Date date;


    @ManyToOne
    @JoinColumn(name="competition_id", nullable=false)
    private Competition competition_id;
    @ManyToOne
    @JoinColumn(name="participant_id", nullable=false)
    private Participant participant_id;


}
