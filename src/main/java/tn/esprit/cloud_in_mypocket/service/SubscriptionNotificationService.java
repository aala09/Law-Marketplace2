package tn.esprit.cloud_in_mypocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.repository.PaiementRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SubscriptionNotificationService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private EmailServiceAbonnement emailServiceAbonnement;

    @Scheduled(cron = "0 0 * * * *") // Exécuté tous les jours à 9h
    public void checkExpiringSubscriptions() {
        System.out.println("Tâche planifiée exécutée !");
        List<Paiement> activeSubscriptions = paiementRepository.findActiveSubscriptions();

        for (Paiement subscription : activeSubscriptions) {
            LocalDateTime expirationDateTime = subscription.getDatePaiement().plusDays(subscription.getPackAbonnement().getDuree());
            LocalDate expirationDate = expirationDateTime.toLocalDate();
            LocalDate today = LocalDate.now();

            long daysUntilExpiration = ChronoUnit.DAYS.between(today, expirationDate);

            // Envoyer des notifications à 7, 3 et 1 jour(s) avant l'expiration
            if (daysUntilExpiration == 7 || daysUntilExpiration == 3 || daysUntilExpiration == 1) {
                emailServiceAbonnement.sendSubscriptionExpirationReminder(
                        subscription.getUtilisateur().getEmail(),
                        subscription.getUtilisateur().getNom() + " " + subscription.getUtilisateur().getPrenom(),
                        subscription.getPackAbonnement().getNom(),
                        (int) daysUntilExpiration
                );
            }

            // Envoyer une notification le jour de l'expiration
            if (daysUntilExpiration == 0) {
                emailServiceAbonnement.sendSubscriptionExpiredNotification(
                        subscription.getUtilisateur().getEmail(),
                        subscription.getUtilisateur().getNom() + " " + subscription.getUtilisateur().getPrenom(),
                        subscription.getPackAbonnement().getNom()
                );
            }
        }
    }



    public Paiement getPaiementById(Long paiementId) {
        return paiementRepository.findById(paiementId).orElse(null);
    }


} 