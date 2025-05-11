package tn.esprit.cloud_in_mypocket.dto;

import lombok.Data;

import java.time.LocalTime;


@Data
public class PresenceRequest {


    private Long employeeId;
    private boolean present;
    private LocalTime heureArrivee;



    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    // Getter et Setter pour present
    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    // Getter et Setter pour heureArrivee
    public LocalTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

}
