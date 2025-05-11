package tn.esprit.cloud_in_mypocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.dto.PresenceDto;
import tn.esprit.cloud_in_mypocket.entity.Employee;
import tn.esprit.cloud_in_mypocket.entity.Presence;
import tn.esprit.cloud_in_mypocket.repository.EmployeeRepositories;
import tn.esprit.cloud_in_mypocket.repository.PresenceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Service

public class PresenceService {

    @Autowired
    private EmployeeRepositories employeeRepositories;

    @Autowired
    private PresenceRepository presenceRepository;

    private final LocalTime heureNormale = LocalTime.of(9, 0); // 09:00

    public List<PresenceDto> getPresencesByDate(LocalDate date) {
        List<Presence> presences = presenceRepository.findByDate(date);
        return presences.stream().map(p -> {
            PresenceDto dto = new PresenceDto();
            dto.setNom(p.getEmployee().getlName());
            dto.setPrenom(p.getEmployee().getfName());
            dto.setDate(p.getDate());
            dto.setHeureArrivee(p.getHeureArrivee());
            if (!p.isPresent()) {
                dto.setStatut("Absent");
            } else if (p.getHeureArrivee().isAfter(heureNormale)) {
                dto.setStatut("En retard");
            } else {
                dto.setStatut("Présent");
            }
            return dto;
        }).toList();
    }

    public void marquerPresenceAutomatique(Employee emp) {
        LocalDate today = LocalDate.now();
        LocalTime heureActuelle = LocalTime.now();

        // Empêcher une double insertion pour le même jour
        boolean dejaMarque = presenceRepository.existsByEmployeeAndDate(emp, today);
        if (dejaMarque) return;

        Presence p = new Presence();
        p.setEmployee(emp);
        p.setDate(today);
        p.setHeureArrivee(heureActuelle);
        p.setPresent(true);

        if (heureActuelle.isAfter(heureNormale)) {
            p.setEnRetard(true);
            emp.setNombreRetards(emp.getNombreRetards() + 1);
        } else {
            p.setEnRetard(false);
        }

        presenceRepository.save(p);
        employeeRepositories.save(emp); // Met à jour l'employé avec les retards
    }

    public Presence enregistrerPresence(Long empId, boolean present, LocalTime heureArrivee) {
        Employee emp = employeeRepositories.findById(empId).orElseThrow();
        Presence p = new Presence();
        p.setEmployee(emp);
        p.setDate(LocalDate.now());
        p.setPresent(present);
        p.setHeureArrivee(heureArrivee);
        return presenceRepository.save(p);
    }


    @Scheduled(cron = "0 0 0 * * *") // 18h00 chaque jour
    public void verifierTempsDeTravail() {
        // LocalTime heureActuelle = LocalTime.now();
        LocalDate today = LocalDate.now();

        List<Employee> employees = employeeRepositories.findAll();
        for (Employee emp : employees) {
            Long totalMinutesWorked = emp.getTotalMinutesWorked();
            Optional<Presence> optionalPresence = presenceRepository.findByEmployeeAndDate(emp, today);
            Presence presence;
            if (optionalPresence.isPresent()) {
                presence = optionalPresence.get();
            } else {
                // Aucune présence enregistrée → créer une nouvelle
                presence = new Presence();
                presence.setEmployee(emp);
                presence.setDate(today);
                presence.setHeureArrivee(LocalTime.of(0, 0)); // Heure fictive
            }
            if (totalMinutesWorked == null) {
                Presence absence = new Presence();
                absence.setEmployee(emp);
                absence.setDate(today);
                absence.setPresent(false);
                absence.setHeureArrivee(LocalTime.of(0, 0));
                presenceRepository.save(absence);

                if (emp.getNombreAbsences() == null) {
                    emp.setNombreAbsences(0);
                }
                emp.setStatus("Absent");
                emp.setNombreAbsences(emp.getNombreAbsences() + 1);
            }
            if (totalMinutesWorked < 60 * 6)
            { // Si le temps total de travail est inférieur à 6 heures
                // Marquer l'employé comme absent



                presence.setPresent(false); // L'employé est absent
                presenceRepository.save(presence);

                // Optionnel : Mettre à jour le statut de l'employé
                emp.setStatus("Absent");
                emp.setTotalMinutesWorked(0L);

                if (emp.getNombreAbsences() == null) {
                    emp.setNombreAbsences(0);
                }
                emp.setNombreAbsences(emp.getNombreAbsences() + 1);
                employeeRepositories.save(emp);
            }
        }

    }
}
