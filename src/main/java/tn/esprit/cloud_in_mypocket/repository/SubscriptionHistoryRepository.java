package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.SubscriptionHistory;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
    // Trouver l'historique des souscriptions d'un utilisateur
    List<SubscriptionHistory> findByUserId(Long userId);
    List<SubscriptionHistory> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
}