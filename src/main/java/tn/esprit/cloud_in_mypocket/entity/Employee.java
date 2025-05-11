package tn.esprit.cloud_in_mypocket.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fName;
    private String lName;
    private String role;

    private Double salary;
    private Double performanceScore;

    private Integer nombreRetards = 0;
    private Integer nombreAbsences = 0;

    private Double notePerformance;



    ///////

    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;
    private Long totalMinutesWorked;

    private Boolean besoinVerification;
    private String codeVerification;
    private LocalDateTime heureVerificationDemandee;

    private String motDePasse;
    private String email;

    @ManyToOne
    Department department;
    private String status;

    public Integer getNombreAbsences() {
        return nombreAbsences;
    }

    public void setNombreAbsences(Integer nombreAbsences) {
        this.nombreAbsences = nombreAbsences;
    }

    public Integer getNombreRetards() {
        return nombreRetards;
    }

    public void setNombreRetards(Integer nombreRetards) {
        this.nombreRetards = nombreRetards;
    }

    public Double getNotePerformance() {
        return notePerformance;
    }

    public void setNotePerformance(Double notePerformance) {
        this.notePerformance = notePerformance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastLogout() {
        return lastLogout;
    }

    private Long idDep;
    public Long getIdDep() {
        return idDep;
    }

    public void setIdDep(Long idDep) { this.idDep = idDep;}


    public void setLastLogout(LocalDateTime lastLogout) {
        this.lastLogout = lastLogout;
    }

    public Long getTotalMinutesWorked() {
        return totalMinutesWorked;
    }

    public void setTotalMinutesWorked(Long totalMinutesWorked) {
        this.totalMinutesWorked = totalMinutesWorked;
    }

    public Boolean getBesoinVerification() {
        return besoinVerification;
    }

    public void setBesoinVerification(Boolean besoinVerification) {
        this.besoinVerification = besoinVerification;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public LocalDateTime getHeureVerificationDemandee() {
        return heureVerificationDemandee;
    }

    public void setHeureVerificationDemandee(LocalDateTime heureVerificationDemandee) {
        this.heureVerificationDemandee = heureVerificationDemandee;
    }


    /////////////


    public String getfName() {
        return fName;
    }
    public String getlName() {
        return lName;
    }
    public void setFName(String fName) {
        this.fName = fName;
    }



    public void setlName(String lName) {
        this.lName = lName;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Double getSalary() {
        return salary;
    }
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    public Double getPerformanceScore() {
        return performanceScore;
    }
    public void setPerformanceScore(Double performanceScore) {
        this.performanceScore = performanceScore;
    }

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Presence> presences = new ArrayList<>();

}
