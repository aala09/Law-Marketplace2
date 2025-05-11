package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.dto.PresenceDto;
import tn.esprit.cloud_in_mypocket.dto.PresenceRequest;
import tn.esprit.cloud_in_mypocket.entity.Presence;
import tn.esprit.cloud_in_mypocket.service.PresenceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/presences")
public class PresenceController {


    @Autowired
    private PresenceService presenceService;

    @GetMapping("/jour/{date}")
    public ResponseEntity<List<PresenceDto>> getPresencesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(presenceService.getPresencesByDate(date));
    }

    @PostMapping("/marquer")
    public ResponseEntity<Presence> enregistrerPresence(@RequestBody PresenceRequest request) {
        return ResponseEntity.ok(
                presenceService.enregistrerPresence(request.getEmployeeId(), request.isPresent(), request.getHeureArrivee())
        );
    }
}
