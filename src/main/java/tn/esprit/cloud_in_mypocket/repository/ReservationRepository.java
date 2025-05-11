package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByFormationId(Long formationId);
    @Query("SELECT r.formation.id, COUNT(r) FROM Reservation r GROUP BY r.formation.id")
    List<Object[]> countReservationsByFormation();

    Reservation findByApprenantIdAndFormationId(Long apprenantId, Long id);
}
