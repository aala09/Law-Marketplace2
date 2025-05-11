package tn.esprit.cloud_in_mypocket.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PresenceDto {

    private String nom;
    private String prenom;
    private LocalDate date;
    private LocalTime heureArrivee;
    private String statut; // Présent / En retard / Absent

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeureArrivee() { return heureArrivee; }
    public void setHeureArrivee(LocalTime heureArrivee) { this.heureArrivee = heureArrivee; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

}
