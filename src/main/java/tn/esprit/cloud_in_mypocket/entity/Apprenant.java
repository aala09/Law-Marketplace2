package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apprenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    @OneToMany(mappedBy = "apprenant")
    public List<Reservation> getReservation() {
        if (reservation == null) {
            reservation = new ArrayList<>();
        }
        return reservation;
    }


    public Long getId() {
        return id;
    }
    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL)
    private List<Reservation> reservation = new ArrayList<>();



    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
