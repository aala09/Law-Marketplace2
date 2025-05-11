package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.cloud_in_mypocket.entity.Department;
import tn.esprit.cloud_in_mypocket.entity.Employee;
import tn.esprit.cloud_in_mypocket.entity.Presence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Transactional
@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {
    List<Presence> findByDate(LocalDate date);
    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
    Optional<Presence> findByEmployeeAndDate(Employee employee, LocalDate date);

}
