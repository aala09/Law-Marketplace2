package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.dto.VerificationRequest;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.payload.LoginRequest;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import tn.esprit.cloud_in_mypocket.service.EmailService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
// Si vous avez une configuration CORS globale, vous pouvez retirer cette annotation
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Utilisateur non trouvé"));
        }

        User user = optionalUser.get();

        // 🛠️ Logs de débogage
        System.out.println("======== DEBUG LOGIN ========");
        System.out.println("Email reçu : " + loginRequest.getEmail());
        System.out.println("Mot de passe reçu : " + loginRequest.getMotDePasse());
        System.out.println("Mot de passe en base : " + user.getMotDePasse());
        System.out.println("Match ? " + passwordEncoder.matches(loginRequest.getMotDePasse(), user.getMotDePasse()));
        System.out.println("================================");

        if (!passwordEncoder.matches(loginRequest.getMotDePasse(), user.getMotDePasse())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Mot de passe incorrect"));
        }

        // ✅ Mise à jour de la date de dernière connexion
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        // ✅ On masque le mot de passe dans la réponse
        user.setMotDePasse(null);

        return ResponseEntity.ok(user);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Utilisateur introuvable"));
        }

        User user = optionalUser.get();

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(request.getCode())) {
            user.setEmailVerified(true);
            user.setVerificationCode(null); // on nettoie le code
            userRepository.save(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "Email vérifié avec succès"));
        } else {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Code incorrect"));
        }
    }
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Utilisateur introuvable"));
        }

        User user = optionalUser.get();
        String code = String.format("%06d", new Random().nextInt(999999));
        user.setResetCode(code);
        userRepository.save(user);

        emailService.sendResetCode(email, code); // méthode à créer
        return ResponseEntity.ok(Collections.singletonMap("message", "Code envoyé à votre email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");
        String newPassword = payload.get("newPassword");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Utilisateur introuvable"));
        }

        User user = optionalUser.get();

        if (!code.equals(user.getResetCode())) {
            return ResponseEntity.status(400).body(Collections.singletonMap("error", "Code invalide"));
        }

        user.setMotDePasse(passwordEncoder.encode(newPassword));
        user.setResetCode(null); // supprimer le code
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "Mot de passe réinitialisé avec succès"));
    }


}