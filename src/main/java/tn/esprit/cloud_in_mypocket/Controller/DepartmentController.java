package tn.esprit.cloud_in_mypocket.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.entity.Department;
import tn.esprit.cloud_in_mypocket.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class DepartmentController {


    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @PutMapping("/updateDepartment/{id}/{newName}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @PathVariable String newName) {
        try {
            // Appel du service pour mettre à jour le département
            Department updatedDepartment = departmentService.updateDepartment(id, newName);
            return ResponseEntity.ok(updatedDepartment); // Retourne le département mis à jour
        } catch (RuntimeException e) {
            // En cas d'erreur, retourne un code 404 et un message d'erreur
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/departments")
    void createDepartment(@RequestBody Department model) {
        Department department = new Department();
        department.setName(model.getName());
        this.departmentService.createDepartment(department);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Department deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/departments")
    List<Department> Departments(){
        return this.departmentService.getAllDepartments();
    }
    @GetMapping("/departments/{id}")
    Department getDepartmentById(@PathVariable long id){
        return this.departmentService.getDepartmentById(id);

    }
/*
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
*/
}
