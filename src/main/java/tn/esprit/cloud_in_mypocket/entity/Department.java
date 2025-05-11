package tn.esprit.cloud_in_mypocket.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;



@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    public String getName() {
        return name;
    }


    // Setter
    public void setName(String name) {
        this.name = name;
    }
    @OneToMany(mappedBy = "department")
    Collection<Employee> employees = new ArrayList<>();


}
