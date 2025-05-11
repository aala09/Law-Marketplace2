package tn.esprit.cloud_in_mypocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Formation;
import tn.esprit.cloud_in_mypocket.repository.FormationRepository;

import java.util.List;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public Formation getFormationById(Long id) {
        return formationRepository.findById(id).orElse(null);
    }

    public Formation createFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    public Formation updateFormation(Long id, Formation updated) {
        Formation existing = formationRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitre(updated.getTitre());
            existing.setDateDebut(updated.getDateDebut());
            existing.setDateFin(updated.getDateFin());
            return formationRepository.save(existing);
        }
        return null;
    }

    public void deleteFormation(Long id) {
        formationRepository.deleteById(id);
    }

    public List<String> getAllFormationTitres() {
        return formationRepository.findAll()
                .stream()
                .map(Formation::getTitre)
                .toList();
    }

}
