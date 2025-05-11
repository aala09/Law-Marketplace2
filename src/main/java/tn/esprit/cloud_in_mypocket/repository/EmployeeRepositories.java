package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.cloud_in_mypocket.entity.Employee;

import java.util.List;


@Repository

public interface EmployeeRepositories extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e ORDER BY e.performanceScore DESC")
    Page<Employee> findAllOrderByPerformanceScoreDesc(Pageable pageable);


}
