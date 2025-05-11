package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.entity.Seance;
import tn.esprit.cloud_in_mypocket.repository.ApprenantRepository;
import tn.esprit.cloud_in_mypocket.repository.SeanceRepository;
import tn.esprit.cloud_in_mypocket.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seances")
//@CrossOrigin(origins = "*")
public class SeanceController {
    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private EmailServicemaissa emailServicemaissa;

    @Autowired
    private SeanceService seanceService;
@Autowired
private ApprenantRepository apprenantRepository;
@Autowired
private ApprenantService apprenantService;
@Autowired
private SeanceRepository seanceRepository;
@Autowired
private ReservationService reservationService;
    @GetMapping
    public List<Seance> getAll() {
        return seanceService.getAllSeances();
    }

    @GetMapping("/{id}")
    public Seance getById(@PathVariable Long id) {
        return seanceService.getSeanceById(id);
    }

    @PostMapping
    public Seance create(@RequestBody Seance seance) {
        return seanceService.createSeance(seance);
    }

    @PutMapping("/{id}")
    public Seance update(@PathVariable Long id, @RequestBody Seance seance) {
        return seanceService.updateSeance(id, seance);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        seanceService.deleteSeance(id);
    }

    @GetMapping("/formation/{formationId}")
    public List<Seance> getSeancesByFormation(@PathVariable Long formationId) {
        return seanceRepository.findByFormationId(formationId);
    }
    @PostMapping("/complete")
    public ResponseEntity<Map<String, String>> completeSeance(@RequestBody Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        try {
            String apprenantNom = data.get("apprenantNom");
            String apprenantEmail = data.get("apprenantEmail");
            String formationTitre = data.get("formationTitre");

            // Appel du service pour générer le PDF et envoyer le mail
            seanceService.handleSessionCompletion(apprenantNom, apprenantEmail, formationTitre);

            response.put("message", "✅ Email sent with certificate!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();  // Log de l'erreur
            response.put("error", "❌ Error while sending the certificate email.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }





}
