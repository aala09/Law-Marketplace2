package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.entity.Apprenant;
import tn.esprit.cloud_in_mypocket.entity.Formation;
import tn.esprit.cloud_in_mypocket.entity.Reservation;
import tn.esprit.cloud_in_mypocket.repository.ApprenantRepository;
import tn.esprit.cloud_in_mypocket.repository.FormationRepository;
import tn.esprit.cloud_in_mypocket.repository.ReservationRepository;
import tn.esprit.cloud_in_mypocket.service.EmailServicemaissa;
import tn.esprit.cloud_in_mypocket.service.ReservationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
//@CrossOrigin(origins = "*")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmailServicemaissa emailServicemaissa;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> data) {

        String nom = (String) data.get("nom");
        String email = (String) data.get("email");
        Long formationId = Long.parseLong(data.get("formationId").toString());

        Optional<Formation> formationOpt = formationRepository.findById(formationId);
        if (formationOpt.isEmpty()) return ResponseEntity.notFound().build();

        Formation formation = formationOpt.get();

        // Vérifier si des places sont encore disponibles
        long existingReservations = reservationRepository.countByFormationId(formationId);
        if (existingReservations >= formation.getnombreplaces()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Il n'y a plus de places disponibles pour cette formation.");
            return ResponseEntity.badRequest().body(error);
        }

        // Enregistrer l'apprenant
        Apprenant apprenant = new Apprenant();
        apprenant.setNom(nom);
        apprenant.setEmail(email);
        apprenantRepository.save(apprenant);

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setApprenant(apprenant);
        reservation.setFormation(formation);
        reservationRepository.save(reservation);

        // ✅ Envoyer l'e-mail de confirmation
        emailServicemaissa.sendConfirmationEmailmaissa(apprenant.getEmail(), formation.getTitre());


        return ResponseEntity.ok(reservation);

    }

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        try {
            emailServicemaissa.sendConfirmationEmailmaissa("mayssahammami2704@gmail.com", "Formation Test Email");
            return ResponseEntity.ok("✅ Email envoyé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

    @GetMapping("/formation/count")
    public ResponseEntity<Map<Long, Long>> getReservationCountsPerFormation() {
        List<Formation> formations = formationRepository.findAll();
        Map<Long, Long> counts = new HashMap<>();

        for (Formation f : formations) {
            long count = reservationRepository.countByFormationId(f.getId());
            counts.put(f.getId(), count);
        }

        return ResponseEntity.ok(counts);
    }
    @GetMapping("/stats")
    public Map<Long, Long> getReservationStats() {
        List<Object[]> stats = reservationRepository.countReservationsByFormation();
        Map<Long, Long> result = new HashMap<>();
        for (Object[] stat : stats) {
            Long formationId = (Long) stat[0];
            Long count = (Long) stat[1];
            result.put(formationId, count);
        }
        return result;
    }

    @GetMapping("/formation/{id}/count")
    public ResponseEntity<Long> getReservationCountForFormation(@PathVariable Long id) {
        long count = reservationRepository.countByFormationId(id);
        return ResponseEntity.ok(count);
    }






    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }

}
