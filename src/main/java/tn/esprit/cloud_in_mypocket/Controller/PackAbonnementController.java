package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.dto.ResponseDTO;
import tn.esprit.cloud_in_mypocket.entity.PackAbonnement;
import tn.esprit.cloud_in_mypocket.service.PackAbonnementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/packs")
public class PackAbonnementController {

    private static final Logger logger = LoggerFactory.getLogger(PackAbonnementController.class);

    @Autowired
    private PackAbonnementService packAbonnementService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createPack(@RequestBody PackAbonnement packAbonnement) {
        packAbonnementService.savePack(packAbonnement);
        ResponseDTO response = new ResponseDTO("success", "Pack created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats-mensuelles")
    public ResponseEntity<Map<String, Map<String, Long>>> getStatsMensuelles() {
        return ResponseEntity.ok(packAbonnementService.getStatsMensuelles());
    }

    @GetMapping
    public List<PackAbonnement> getAllPacks() {
        return packAbonnementService.getAllPacks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackAbonnement> getPackById(@PathVariable Long id) {
        return packAbonnementService.getPackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePack(@PathVariable Long id, HttpServletRequest request) {
        logger.info("Tentative de suppression du pack avec l'ID: {}", id);
        logger.info("Headers de la requête: {}", request.getHeaderNames());
        logger.info("Méthode de la requête: {}", request.getMethod());
        logger.info("URL de la requête: {}", request.getRequestURL());

        try {
            packAbonnementService.deletePack(id);
            logger.info("Pack supprimé avec succès");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du pack: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/stats")
    public ResponseEntity<ResponseDTO> getStatistiques() {
        Map<String, Long> stats = packAbonnementService.getNombreUtilisateursParType();
        ResponseDTO response = new ResponseDTO("success", "Statistiques récupérées avec succès", stats);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updatePack(@PathVariable Long id, @RequestBody PackAbonnement updatedPack) {
        packAbonnementService.updatePack(id, updatedPack);
        ResponseDTO response = new ResponseDTO("success", "Pack updated successfully");
        return ResponseEntity.ok(response);
    }


}