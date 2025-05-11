package tn.esprit.cloud_in_mypocket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Department;
import tn.esprit.cloud_in_mypocket.repository.DepartmentRepositories;

import java.util.List;

@Service

public class DepartmentService {
    @Autowired
    //final
    private DepartmentRepositories departmentRepositories ;
   /* public  DepartmentService(DepartmentRepositories departmentRepositories){
        this.departmentRepositories = departmentRepositories;

    }*/

    public void createDepartment(Department d) {
        this.departmentRepositories.save( d );
    }




    public Department getDepartmentById(Long id) {
        return this.departmentRepositories.findById(id).get();
    }


    public List<Department> getAllDepartments() {
        return this.departmentRepositories.findAll();
    }





    public Department updateDepartment(Long id, String newName) {
        // Récupérer le département existant depuis la base de données
        Department department = departmentRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found")); // Si le département n'existe pas, lancer une exception

        // Mettre à jour le nom
        department.setName(newName);

        // Sauvegarder et retourner le département mis à jour
        return departmentRepositories.save(department);
    }


    public void deleteDepartment(Long id) {

        if (!departmentRepositories.existsById(id)) {
            throw new RuntimeException("Department not found with ID: " + id);
        }
        departmentRepositories.deleteById(id);

    }


}
