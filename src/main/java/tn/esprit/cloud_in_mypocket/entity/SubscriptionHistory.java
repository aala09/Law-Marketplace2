package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription_history")
public class SubscriptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'utilisateur qui a souscrit au pack

    @ManyToOne
    @JoinColumn(name = "pack_id", nullable = false)
    private PackAbonnement packAbonnement; // Le pack souscrit

    private LocalDate startDate; // Date de début de la souscription
    private LocalDate endDate; // Date de fin de la souscription

    // Constructeur pour faciliter la création d'une entrée d'historique
    public SubscriptionHistory(User user, PackAbonnement packAbonnement, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.packAbonnement = packAbonnement;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}