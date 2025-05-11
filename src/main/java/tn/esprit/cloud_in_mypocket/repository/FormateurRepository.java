package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.Formateur;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
}
