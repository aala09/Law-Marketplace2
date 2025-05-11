package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.Apprenant;

@Repository
public interface ApprenantRepository extends JpaRepository<Apprenant, Long> {
}
