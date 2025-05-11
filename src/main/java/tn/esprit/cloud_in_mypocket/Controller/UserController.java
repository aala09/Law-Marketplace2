package tn.esprit.cloud_in_mypocket.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.dto.ResponseDTO;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import tn.esprit.cloud_in_mypocket.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("✅ Register reçu : " + user.toString());
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoint DELETE pour supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            System.out.println("Deleting user with id: " + id);
            userService.deleteUser(id);
            ResponseDTO response = new ResponseDTO("success", "Pack deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setNom(updatedUser.getNom());
        user.setPrenom(updatedUser.getPrenom());
        user.setEmail(updatedUser.getEmail());
        user.setNumeroDeTelephone(updatedUser.getNumeroDeTelephone());
        user.setAdresseLivraison(updatedUser.getAdresseLivraison());
        user.setRole(updatedUser.getRole());

        user.setPackAbonnement(updatedUser.getPackAbonnement());

        // ✅ Ajoute cette ligne :
        user.setPhoto(updatedUser.getPhoto());
        System.out.println("🔁 Reçu PUT update pour ID = " + id);
        System.out.println("Données reçues : " + updatedUser.toString());

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }


}
