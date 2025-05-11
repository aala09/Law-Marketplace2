package tn.esprit.cloud_in_mypocket.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Apprenant;
import tn.esprit.cloud_in_mypocket.repository.ApprenantRepository;
import tn.esprit.cloud_in_mypocket.repository.ReservationRepository;
import tn.esprit.cloud_in_mypocket.repository.SeanceRepository;


import java.util.List;
@Getter
@Setter
@Service
public class ApprenantService {

    @Autowired
    private ApprenantRepository apprenantRepository;
    @Autowired
    @Lazy  // Lazy initialization of SeanceService to avoid circular dependency

    private SeanceService seanceService;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmailServicemaissa emailServicemaissa;
    @Autowired
    private JavaMailSender javaMailSender;

    public List<Apprenant> getAllApprenants() {
        return apprenantRepository.findAll();
    }

    public Apprenant getApprenantById(Long id) {
        return apprenantRepository.findById(id).orElse(null);
    }

    public Apprenant createApprenant(Apprenant apprenant) {
        return apprenantRepository.save(apprenant);
    }

    public Apprenant updateApprenant(Long id, Apprenant update) {
        Apprenant existing = apprenantRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNom(update.getNom());
            existing.setEmail(update.getEmail()); // si tu veux aussi mettre à jour l'email par exemple
            return apprenantRepository.save(existing);
        }
        return null;
    }

    public void deleteApprenant(Long id) {
        apprenantRepository.deleteById(id);
    }


}


