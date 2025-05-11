package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.cloud_in_mypocket.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // ✅ Récupérer les utilisateurs inactifs depuis une date donnée (last login)
    @Query("SELECT u FROM User u WHERE u.lastLoginDate < :date AND u.active = true")
    List<User> findInactiveUsersSince(@Param("date") LocalDateTime date);

    // Ajoute ici d'autres méthodes personnalisées si besoin...
}
