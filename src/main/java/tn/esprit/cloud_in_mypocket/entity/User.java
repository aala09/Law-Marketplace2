package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String numeroDeTelephone;
    private String resetCode;


    // ✅ 2FA
    private String verificationCode;
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Long lawFirmId;
    private String adresseLivraison;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "photo", nullable = true)
    private String photo;

    // ✅ Renommé "active" pour éviter conflits avec "isActive"
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "pack_abonnement_id")
    private PackAbonnement packAbonnement;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionHistory> subscriptionHistory;

    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.CLIENT;
        }
        if (lastLoginDate == null) {
            lastLoginDate = LocalDateTime.now();
        }
    }
}
