package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesPackAbonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typePack;
    private Long nombreUtilisateurs;
    private String date;

    @Override
    public String toString() {
        return "StatistiquesPackAbonnement{" +
                "typePack='" + typePack + '\'' +
                ", nombreUtilisateurs=" + nombreUtilisateurs +
                ", date='" + date + '\'' +
                '}';
    }
}