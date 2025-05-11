package tn.esprit.cloud_in_mypocket.service;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.cloud_in_mypocket.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    @Transactional
    void createEmployee(Employee e);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    void updateEmployee(Long id, Map<String, Object> updates);
    void deleteEmployee(Long id);
    void evaluatePerformance(Long Id, Double newScore);
    List<Employee> getTopPerformers(int topN);
    List<Employee> findByEmail(String email);

    void updateEmployee(String fName, String lName, Map<String, Object> updates);

    void verifierEmployesActifs();
    //List<Employee> findAll();

}
