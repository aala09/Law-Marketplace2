package tn.esprit.cloud_in_mypocket.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.entity.PackAbonnement;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.PaiementRepository;
import tn.esprit.cloud_in_mypocket.repository.PackAbonnementRepository;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PackAbonnementRepository packAbonnementRepository;

    @Autowired
    private UserRepository userRepository;
    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }
    public double calculateAmount(Long packId, boolean isYearly) {
        PackAbonnement pack = packAbonnementRepository.findById(packId)
                .orElseThrow(() -> new RuntimeException("Pack not found"));
        return isYearly ? pack.getPrixAnnuel() : pack.getPrixMensuel();
    }

    public void processPayment(String paymentIntentId, Long packId, Long userId, boolean isYearly) {
        try {
            log.info("Processing payment with intentId: {}, packId: {}, userId: {}, isYearly: {}",
                    paymentIntentId, packId, userId, isYearly);

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            log.info("Retrieved payment intent with status: {}", paymentIntent.getStatus());

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                log.error("Payment not successful. Current status: {}", paymentIntent.getStatus());
                throw new RuntimeException("Payment not successful. Current status: " + paymentIntent.getStatus());
            }

            PackAbonnement pack = packAbonnementRepository.findById(packId)
                    .orElseThrow(() -> {
                        log.error("Pack not found with id: {}", packId);
                        return new RuntimeException("Pack not found");
                    });

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User not found with id: {}", userId);
                        return new RuntimeException("User not found");
                    });

            Paiement paiement = new Paiement();
            paiement.setMontant(paymentIntent.getAmount() / 100.0);
            paiement.setStripePaymentId(paymentIntentId);
            paiement.setStatus(paymentIntent.getStatus());
            paiement.setDatePaiement(LocalDateTime.now());
            paiement.setIsYearly(isYearly);
            paiement.setPackAbonnement(pack);
            paiement.setUtilisateur(user);

            paiementRepository.save(paiement);
            log.info("Payment processed and saved successfully");
        } catch (StripeException e) {
            log.error("Stripe error while processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error processing payment: " + e.getMessage(), e);
        }
    }

    public List<Paiement> getUserSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paiementRepository.findByUtilisateurOrderByDatePaiementDesc(user);
    }
}