package tn.esprit.cloud_in_mypocket.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Role;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Un compte avec cet email existe déjà.");
        }

        if (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getMotDePasse());
            user.setMotDePasse(hashedPassword);
        }

        user.setRole(Role.CLIENT);

        String code = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(code);
        user.setEmailVerified(false);

        // ✅ Envoi email de vérification
        emailService.sendVerificationCode(user.getEmail(), code);

        return userRepository.save(user);
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Méthode pour supprimer un utilisateur par son id
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    @PostConstruct
    public void createTestUser() {
        if (!userRepository.findByEmail("admin@test.com").isPresent()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setNom("Admin");
            user.setPrenom("Test");
            user.setRole(Role.ADMIN);
            user.setMotDePasse(passwordEncoder.encode("123456")); // 🔐 ici le mot de passe est bien encodé
            userRepository.save(user);
            System.out.println("✅ Utilisateur de test créé avec email: admin@test.com | mot de passe: 123456");
        }
    }

}
