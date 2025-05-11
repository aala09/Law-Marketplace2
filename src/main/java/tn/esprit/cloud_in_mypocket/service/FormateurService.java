package tn.esprit.cloud_in_mypocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Formateur;
import tn.esprit.cloud_in_mypocket.repository.FormateurRepository;


import java.util.List;

@Service
public class FormateurService {
    @Autowired
    private FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    public Formateur getFormateurById(Long id) {
        return formateurRepository.findById(id).orElse(null);
    }

    public Formateur createFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Formateur updateFormateur(Long id, Formateur updatedFormateur) {
        Formateur existing = formateurRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(updatedFormateur.getName());
            existing.setName(updatedFormateur.getName());
            existing.setLastname(updatedFormateur.getLastname());

            // update other fields as needed
            return formateurRepository.save(existing);
        }
        return null;
    }

    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }
}

