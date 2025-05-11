package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.cloud_in_mypocket.entity.Department;

import java.util.Optional;

@Transactional
@Repository
public interface DepartmentRepositories extends JpaRepository<Department, Long > {
    Optional<Department> findById(Long id);

}
