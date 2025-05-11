package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tn.esprit.cloud_in_mypocket.entity.Formation;
import tn.esprit.cloud_in_mypocket.service.FormationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/formations")
//@CrossOrigin(origins = "*")
public class FormationController{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FormationService formationService;

    @GetMapping
    public List<Formation> getAll() {
        return formationService.getAllFormations();
    }

    @GetMapping("/{id}")
    public Formation getById(@PathVariable Long id) {
        return formationService.getFormationById(id);
    }

    @PostMapping
    public Formation create(@RequestBody Formation formation) {
        return formationService.createFormation(formation);
    }

    @PutMapping("/{id}")
    public Formation update(@PathVariable Long id, @RequestBody Formation formation) {
        return formationService.updateFormation(id, formation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        formationService.deleteFormation(id);
    }
    @PostMapping("/recommend-ai")
    public ResponseEntity<?> getAIRecommendations(@RequestBody Map<String, Long> request) {
        Long formationId = request.get("id");

        // Call the correct AI URL with GET
        String aiServiceUrl = "http://localhost:5000/recommend/" + formationId;

        // Send GET request without body
        ResponseEntity<String> response = restTemplate.getForEntity(aiServiceUrl, String.class);

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/recommend-ai/{id}")
    public ResponseEntity<?> getAIRecommendations(@PathVariable Long id) {
        String aiServiceUrl = "http://localhost:5000/recommend/" + id;

        ResponseEntity<String> response = restTemplate.getForEntity(aiServiceUrl, String.class);

        return ResponseEntity.ok(response.getBody());
    }
    @PostMapping("/chatbot")
    public ResponseEntity<?> chatWithBot(@RequestBody Map<String, String> messageBody) {
        String message = messageBody.get("message");

        String flaskUrl = "http://localhost:5001/chatbot";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("message", message);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error contacting the chatbot service: " + e.getMessage());
        }
    }


}
