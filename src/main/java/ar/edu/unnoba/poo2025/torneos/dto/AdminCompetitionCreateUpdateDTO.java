package ar.edu.unnoba.poo2025.torneos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminCompetitionCreateUpdateDTO { //se utiliza para crear/actualizar una competencia dentro de un torneo
    private String name;
    private int quota;
    private double base_price;  //TODO OKEY CREO QUE SE ME OCURRIO UNA IDEA
                                //TODO En lugar de tener este dto, podriamos usar el dto de AdminCompetitionDetailDTO
                                //TODO Y en lugar de pasarle todos los atributos que tiene (id, total_registrations, total_amount, etc), pasarle solo lo que queremos de aqui (name, quota, base_price).
                                //TODO El problema que me imagino es que 1-deberiamos cambiarle el nombre al dto que usemos. 2-Si no le ponemos todos los atributos al dto, los demas atributos apareceran como null? eso afectara en algo? Para pensar
}
