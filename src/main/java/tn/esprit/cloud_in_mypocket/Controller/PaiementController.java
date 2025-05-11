package tn.esprit.cloud_in_mypocket.Controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.dto.StripeResponseDTO;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.payload.PaymentRequest;
import tn.esprit.cloud_in_mypocket.service.PaiementService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/paiements")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @PostMapping("/create-intent")
    public ResponseEntity<StripeResponseDTO> createPaymentIntent(
            @RequestParam Long packId,
            @RequestParam Long userId,
            @RequestParam boolean isYearly,
            @RequestBody PaymentRequest request) {
        try {
            log.info("Creating payment intent for packId: {}, userId: {}, isYearly: {}", packId, userId, isYearly);

            // Calculate amount based on the request
            double amount = request.getAmount();

            // Create payment intent with Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount)
                    .setCurrency("eur")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("packId", packId.toString())
                    .putMetadata("userId", userId.toString())
                    .putMetadata("isYearly", String.valueOf(isYearly))
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("Payment intent created successfully with id: {}", paymentIntent.getId());

            return ResponseEntity.ok(StripeResponseDTO.fromPaymentIntent(paymentIntent));
        } catch (StripeException e) {
            log.error("Error creating payment intent: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(StripeResponseDTO.error(e.getMessage()));
        }
    }


    @PostMapping("/process")
    public ResponseEntity<?> processPayment(
            @RequestParam String paymentIntentId,
            @RequestParam Long packId,
            @RequestParam Long userId,
            @RequestParam boolean isYearly) {
        try {
            log.info("Processing payment with intentId: {}, packId: {}, userId: {}, isYearly: {}",
                    paymentIntentId, packId, userId, isYearly);
            paiementService.processPayment(paymentIntentId, packId, userId, isYearly);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<Paiement>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(paiementService.getUserSubscriptions(userId));
    }

    @GetMapping("/users/{userId}/active-subscription")
    public ResponseEntity<Paiement> getActiveSubscription(@PathVariable Long userId) {
        List<Paiement> subscriptions = paiementService.getUserSubscriptions(userId);
        if (subscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Retourner le paiement le plus récent comme abonnement actif
        return ResponseEntity.ok(subscriptions.get(0));
    }

    @PostMapping
    public ResponseEntity<Paiement> savePaiement(@RequestBody Paiement paiement) {
        Paiement savedPaiement = paiementService.savePaiement(paiement);
        return ResponseEntity.ok(savedPaiement);
    }
}