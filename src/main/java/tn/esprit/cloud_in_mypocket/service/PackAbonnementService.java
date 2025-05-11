package tn.esprit.cloud_in_mypocket.service;

import tn.esprit.cloud_in_mypocket.entity.PackAbonnement;
import tn.esprit.cloud_in_mypocket.entity.StatistiquesPackAbonnement;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.repository.PackAbonnementRepository;
import tn.esprit.cloud_in_mypocket.repository.StatistiquesPackAbonnementRepository;
import tn.esprit.cloud_in_mypocket.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PackAbonnementService  {

    private static final Logger logger = LoggerFactory.getLogger(PackAbonnementService.class);

    @Autowired
    private PackAbonnementRepository packAbonnementRepository;

    @Autowired
    private StatistiquesPackAbonnementRepository statistiquesPackAbonnementRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    public PackAbonnement savePack(PackAbonnement packAbonnement) {
        return packAbonnementRepository.save(packAbonnement);
    }

    public List<PackAbonnement> getAllPacks() {
        return packAbonnementRepository.findAll();
    }

    public Optional<PackAbonnement> getPackById(Long id) {
        return packAbonnementRepository.findById(id);
    }

    public void deletePack(Long id) {
        logger.info("Début de la suppression du pack avec l'ID: {}", id);

        // Récupérer tous les paiements associés à ce pack
        List<Paiement> paiementsAssocies = paiementRepository.findByPackAbonnementId(id);
        logger.info("Nombre de paiements associés trouvés: {}", paiementsAssocies.size());

        // Supprimer tous les paiements associés
        if (!paiementsAssocies.isEmpty()) {
            logger.info("Suppression des paiements associés...");
            paiementRepository.deleteAll(paiementsAssocies);
        }

        // Maintenant, supprimer le pack
        logger.info("Suppression du pack...");
        packAbonnementRepository.deleteById(id);
        logger.info("Pack supprimé avec succès");
    }

    public Map<String, Long> getNombreUtilisateursParType() {
        // Récupérer tous les paiements valides
        List<Paiement> paiements = paiementRepository.findAll();
        Map<String, Long> stats = new HashMap<>();

        logger.info("Nombre total de paiements trouvés: {}", paiements.size());

        for (Paiement paiement : paiements) {
            if (paiement.getPackAbonnement() != null) {
                String type = paiement.getPackAbonnement().getType().toString();
                // Compter chaque paiement comme un nouvel abonnement
                stats.put(type, stats.getOrDefault(type, 0L) + 1);
                logger.info("Paiement {}: type={}, montant={}, date={}",
                        paiement.getId(), type, paiement.getMontant(), paiement.getDatePaiement());
            }
        }

        logger.info("Statistiques finales basées sur les paiements: {}", stats);
        return stats;
    }

    @Scheduled(fixedRate = 300000) // Exécute toutes les 5 minutes
    public void afficherStatistiquesUtilisateurs() {
        logger.info("Début de la mise à jour des statistiques basées sur les paiements");
        Map<String, Long> stats = getNombreUtilisateursParType();

        if (stats.isEmpty()) {
            logger.warn("Aucune statistique trouvée dans les paiements");
            return;
        }

        // Enregistrer les statistiques dans la base de données
        for (Map.Entry<String, Long> entry : stats.entrySet()) {
            StatistiquesPackAbonnement statistique = new StatistiquesPackAbonnement();
            statistique.setTypePack(entry.getKey());
            statistique.setNombreUtilisateurs(entry.getValue());
            statistique.setDate(LocalDateTime.now().toString());
            statistiquesPackAbonnementRepository.save(statistique);
            logger.info("Statistique enregistrée: {}", statistique);
        }
        logger.info("Fin de la mise à jour des statistiques");
    }

    @Scheduled(cron = "0 0/30 * * * *") // Exécute chaque début d'heure
    public void verifierPacksSansUtilisateurs() {
        List<PackAbonnement> packs = packAbonnementRepository.findAll();
        List<PackAbonnement> packsVides = packs.stream()
                .filter(p -> p.getUsers() == null || p.getUsers().isEmpty())
                .collect(Collectors.toList());

        if (!packsVides.isEmpty()) {
            logger.warn("⚠️ Packs sans utilisateurs détectés : {}", packsVides);
        } else {
            logger.info("✅ Tous les packs ont au moins un utilisateur.");
        }

    }

    public PackAbonnement updatePack(Long id, PackAbonnement updatedPack) {
        Optional<PackAbonnement> existingPackOpt = packAbonnementRepository.findById(id);

        if (existingPackOpt.isPresent()) {
            PackAbonnement existingPack = existingPackOpt.get();

            // Mise à jour des champs
            existingPack.setNom(updatedPack.getNom());
            existingPack.setDescription(updatedPack.getDescription());

            existingPack.setPrixAnnuel(updatedPack.getPrixAnnuel());
            existingPack.setPrixMensuel(updatedPack.getPrixMensuel());
            existingPack.setDuree(updatedPack.getDuree());
            existingPack.setType(updatedPack.getType());

            // Sauvegarder les modifications
            return packAbonnementRepository.save(existingPack);
        } else {
            throw new RuntimeException("Pack avec l'ID " + id + " introuvable.");
        }
    }

    public Map<String, Map<String, Long>> getStatsMensuelles() {
        // Récupérer les paiements groupés par mois et type de pack
        List<Paiement> paiements = paiementRepository.findAll();

        Map<String, Map<String, Long>> result = paiements.stream()
                .filter(p -> p.getPackAbonnement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.groupingBy(
                                p -> p.getPackAbonnement().getType().toString(),
                                Collectors.counting()
                        )
                ));

        logger.info("Statistiques mensuelles basées sur les paiements: {}", result);
        return result;
    }
    public Long getMostPurchasedPackId() {
        return paiementRepository.findMostPurchasedPackId(PageRequest.of(0, 1)).get(0);
    }
}