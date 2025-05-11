package tn.esprit.cloud_in_mypocket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Department;
import tn.esprit.cloud_in_mypocket.entity.Employee;
import tn.esprit.cloud_in_mypocket.entity.Presence;
import tn.esprit.cloud_in_mypocket.repository.DepartmentRepositories;
import tn.esprit.cloud_in_mypocket.repository.EmployeeRepositories;
import tn.esprit.cloud_in_mypocket.repository.PresenceRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private EmployeeRepositories employeeRepository;  // Pour Employee

    @Autowired
    private DepartmentRepositories departmentRepository;

    private final EmployeeRepositories employeeRepositories;


    private Random random = new Random();

    @Autowired
    public EmployeeServiceImpl(EmployeeRepositories employeeRepositories,
                               PresenceRepository presenceRepository,
                               DepartmentRepositories departmentRepository) {
        this.employeeRepositories = employeeRepositories;
        this.presenceRepository = presenceRepository;
        this.departmentRepository = departmentRepository;
    }
    @Scheduled(fixedRate = 60000) // Chaque 1 minute
    public void verifierEtDeconnecterEmployesNonVerifies() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        List<Employee> employes = employeeRepositories.findAll();
        for (Employee employe : employes) {

            if (Boolean.TRUE.equals(employe.getBesoinVerification())
                    && employe.getHeureVerificationDemandee() != null) {

                Duration duration = Duration.between(employe.getHeureVerificationDemandee(), now);

                if (duration.toMinutes() >= 2) {
                    Optional<Presence> optionalPresence = presenceRepository.findByEmployeeAndDate(employe, today);
                    optionalPresence.ifPresent(presence -> {
                        //Presence presence = optionalPresence.get();
                        presence.setPresent(false);
                        presenceRepository.save(presence);
                    });

                    employe.setStatus("Absent");



                    employe.setLastLogout(now);
                    Duration duree = Duration.between(employe.getLastLogin(), now);
                    long minutes = duree.toMinutes();
                    if (employe.getNombreAbsences() == null) {
                        employe.setNombreAbsences(0);
                    }
                    employe.setNombreAbsences(employe.getNombreAbsences() + 1);
                    if (employe.getTotalMinutesWorked() == null) employe.setTotalMinutesWorked(0L);
                    employe.setTotalMinutesWorked(employe.getTotalMinutesWorked() + minutes);

                    employe.setBesoinVerification(false);
                    employe.setCodeVerification(null);
                    employe.setHeureVerificationDemandee(null);

                    employeeRepositories.save(employe);

                    System.out.println("🕒 Déconnexion automatique de l'employé " );
                }
            }
        }
    }
    @Scheduled(fixedRate = 180000)//3 * 60 * 60 * 1000) // toutes les 3 heures
    public void verifierEmployesActifs() {
        List<Employee> employes = employeeRepositories.findAll();

        for (Employee emp : employes) {
            if (emp.getLastLogin() != null && emp.getLastLogout() == null) {
                // L'utilisateur est connecté
                String code = String.format("%06d", random.nextInt(999999));
                emp.setBesoinVerification(true);
                emp.setCodeVerification(code);
                emp.setHeureVerificationDemandee(LocalDateTime.now());
                employeeRepositories.save(emp);

                // Tu peux aussi ici envoyer un mail ou créer une alerte frontend
                System.out.println("Demande de code pour " + emp.getfName() +emp.getlName() + ": " + code);
            }
        }
    }
    public EmployeeServiceImpl(EmployeeRepositories employeeRepositories){
        this.employeeRepositories = employeeRepositories;
    }
    @Override
    public void createEmployee(Employee e) {this.employeeRepositories.save(e);

    }



    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepositories.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return this.employeeRepositories.findById(id).get();
    }

    @Override
    public void updateEmployee(Long id, Map<String, Object> updates) {
        Employee employee = employeeRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Mise à jour dynamique des champs
        updates.forEach((key, value) -> {
            switch (key) {
                case "role":
                    employee.setRole((String) value);
                    break;
                case "salary":
                    employee.setSalary(Double.parseDouble(value.toString()));
                    break;
                case "departmentId":
                    Department department = departmentRepository.findById(Long.parseLong(value.toString()))
                            .orElseThrow(() -> new RuntimeException("Department not found"));
                    employee.setDepartment(department);
                    break;
                case "email":
                    employee.setEmail((String) value);
                    break;
                default:
                    throw new RuntimeException("Field " + key + " cannot be updated");
            }
        });

        employeeRepositories.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepositories.existsById(id)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
        employeeRepositories.deleteById(id);

    }

    @Override
    public void evaluatePerformance(Long id, Double newScore) {
        Employee employee = employeeRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        employee.setPerformanceScore(newScore);
        employeeRepositories.save(employee);
    }

    @Override
    public List<Employee> getTopPerformers(int topN) {
        if (topN <= 0) {
            throw new IllegalArgumentException("topN doit être supérieur à 0");
        }

        Pageable pageable = PageRequest.of(0, topN); // Page 0, taille = topN
        Page<Employee> page = employeeRepositories.findAllOrderByPerformanceScoreDesc(pageable);
        return page.getContent();
    }

    @Override
    public List<Employee> findByEmail(String email) {
        return employeeRepositories.findByEmail(email);
    }

    @Override
    public void updateEmployee(String fName, String lName, Map<String, Object> updates) {

    }

    @Scheduled(cron = "0 0 18 * * *") // tous les jours à 18h
    public void verifierAbsencesEtRetards() {
        LocalDateTime today = LocalDateTime.now();
        for (Employee emp : employeeRepositories.findAll()) {
            if (emp.getLastLogin() == null || !emp.getLastLogin().toLocalDate().equals(today.toLocalDate())) {
                // Absence
                emp.setNombreAbsences(emp.getNombreAbsences() + 1);
            } else if (emp.getLastLogin().toLocalTime().isAfter(LocalTime.of(9, 0))) {
                // Retard après 9h
                emp.setNombreRetards(emp.getNombreRetards() + 1);
            }

            // Calcul de la note automatique simple
            double note = 10.0; // par défaut
            note -= emp.getNombreAbsences() * 2;
            note -= emp.getNombreRetards() * 0.5;
            emp.setNotePerformance(Math.max(note, 0.0));

            employeeRepositories.save(emp);
        }
    }

}
