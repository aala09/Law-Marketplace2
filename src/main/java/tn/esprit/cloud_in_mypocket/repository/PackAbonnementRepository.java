package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.PackAbonnement;

@Repository
public interface PackAbonnementRepository extends JpaRepository<PackAbonnement, Long> {
}