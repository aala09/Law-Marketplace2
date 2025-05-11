package tn.esprit.cloud_in_mypocket.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSeance;

    @ManyToOne
    @JoinColumn(name = "formation_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Formation formation;

    public LocalDate getDateSeance() {
        return dateSeance;
    }

    // Setter
    public void setDateSeance(LocalDate dateSeance) {
        this.dateSeance = dateSeance;
    }
    public Formation getFormation() {
        return formation;
    }
    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Long getId() {
                return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
