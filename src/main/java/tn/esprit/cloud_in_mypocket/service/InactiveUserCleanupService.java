package tn.esprit.cloud_in_mypocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
//genere auto un logger via lambok
@Slf4j
public class InactiveUserCleanupService {
    //pour acceder a la base de données

    private final UserRepository userRepository;
    //on lit a partir application properties
    
    @Value("${app.user.inactive-days-threshold:90}")
    private int inactiveDaysThreshold;

    public InactiveUserCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Exécute à minuit tous les jours
    public void cleanupInactiveUsers() {
        log.info("Starting inactive users cleanup process");
        
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(inactiveDaysThreshold);
        List<User> inactiveUsers = userRepository.findInactiveUsersSince(thresholdDate);
        
        log.info("Found {} inactive users to be processed", inactiveUsers.size());
        
        for (User user : inactiveUsers) {
            user.setActive(false);
            userRepository.save(user);
            log.info("Deactivated user with ID: {} and email: {}", user.getId(), user.getEmail());
        }
        
        log.info("Completed inactive users cleanup process");
    }
} 