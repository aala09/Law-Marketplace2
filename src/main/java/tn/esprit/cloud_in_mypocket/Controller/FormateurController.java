package tn.esprit.cloud_in_mypocket.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.entity.Formateur;
import tn.esprit.cloud_in_mypocket.service.FormateurService;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class FormateurController {

    @Autowired
    private FormateurService formateurService;

    @GetMapping
    public List<Formateur> getAll() {
        return formateurService.getAllFormateurs();
    }

    @GetMapping("/{id}")
    public Formateur getById(@PathVariable Long id) {
        return formateurService.getFormateurById(id);
    }

    @PostMapping("/formateurs")
    public Formateur create(@Valid @RequestBody Formateur formateur) {
        return formateurService.createFormateur(formateur);
    }

    @PutMapping("/{id}")
    public Formateur update(@PathVariable Long id, @RequestBody Formateur formateur) {
        return formateurService.updateFormateur(id, formateur);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
    }
}
