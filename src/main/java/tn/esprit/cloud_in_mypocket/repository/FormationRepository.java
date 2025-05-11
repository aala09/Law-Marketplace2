package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.Formation;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
}
