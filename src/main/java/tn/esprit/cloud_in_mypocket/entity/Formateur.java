package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;


    private String lastname;



    private String email;

    @OneToMany(mappedBy = "formateur")
    private List<Formation> formations;
    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }
    public void setid(Long id) {
        this.id = id;
    }
    public Long getid() {
        return id;
    }



}
