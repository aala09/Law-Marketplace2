package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private int nombreplaces;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;


    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Formateur formateur;

    @OneToMany(mappedBy = "formation")
    private List<Seance> seances;


    @OneToMany(mappedBy = "formation")

    private List<Reservation> reservations;
    private int placesdisponibles; // on va diminuer celui-là

    // Getters & Setters
    public int getPlacesdisponibles() {
        return placesdisponibles;
    }

    public void setPlacesdisponibles(int placesdisponibles) {
        this.placesdisponibles = placesdisponibles;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setdescription(String description) {
        this.description = description;}
    public String getdescription() {
        return description;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    public LocalDate getDateFin() {
        return dateFin;
    }
    public void setnombreplaces(int nombreplaces) {
        this.nombreplaces = nombreplaces;
    }
    public int getnombreplaces() {
        return nombreplaces;
    }

}
