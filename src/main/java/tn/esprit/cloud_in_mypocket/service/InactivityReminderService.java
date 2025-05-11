package tn.esprit.cloud_in_mypocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class InactivityReminderService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.user.inactivity-reminder-days:7}")
    private int inactivityReminderDays;

    @Autowired
    public InactivityReminderService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 10 * * ?") // Exécute à 10h00 tous les jours
    public void sendInactivityReminders() {
        log.info("Starting inactivity reminder process");
        
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(inactivityReminderDays);
        List<User> inactiveUsers = userRepository.findInactiveUsersSince(thresholdDate);
        
        log.info("Found {} inactive users to send reminders", inactiveUsers.size());
        
        for (User user : inactiveUsers) {
            try {
                emailService.sendInactivityReminder(user.getEmail(), user.getNom() + " " + user.getPrenom());
                log.info("Sent inactivity reminder to user: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send reminder to user {}: {}", user.getEmail(), e.getMessage());
            }
        }
        
        log.info("Completed inactivity reminder process");
    }
} 