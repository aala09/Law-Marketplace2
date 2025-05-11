package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import tn.esprit.cloud_in_mypocket.service.InactiveUserCleanupService;
import tn.esprit.cloud_in_mypocket.service.InactivityReminderService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InactiveUserCleanupService inactiveUserCleanupService;

    @Autowired
    private InactivityReminderService inactivityReminderService;

    @PostMapping("/set-last-login/{userId}")
    public ResponseEntity<?> setLastLoginDate(
            @PathVariable Long userId,
            @RequestParam int daysAgo) {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setLastLoginDate(LocalDateTime.now().minusDays(daysAgo));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Last login date updated successfully");
        response.put("userId", userId);
        response.put("newLastLoginDate", user.getLastLoginDate());
        response.put("isActive", user.getActive()); // ✅ corrigé

        return ResponseEntity.ok(response);
    }

    @PostMapping("/trigger-cleanup")
    public ResponseEntity<?> triggerCleanup() {
        inactiveUserCleanupService.cleanupInactiveUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cleanup process triggered successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trigger-reminders")
    public ResponseEntity<?> triggerReminders() {
        inactivityReminderService.sendInactivityReminders();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reminder process triggered successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-status/{userId}")
    public ResponseEntity<?> getUserStatus(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("email", user.getEmail());
        response.put("lastLoginDate", user.getLastLoginDate());
        response.put("isActive", user.getActive()); // ✅ corrigé

        return ResponseEntity.ok(response);
    }
}
