package tn.esprit.cloud_in_mypocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cloud_in_mypocket.entity.Reservation;
import tn.esprit.cloud_in_mypocket.repository.ApprenantRepository;
import tn.esprit.cloud_in_mypocket.repository.ReservationRepository;
import tn.esprit.cloud_in_mypocket.repository.SeanceRepository;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SeanceService seanceService;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private EmailServicemaissa emailServicemaissa;
@Autowired
private ApprenantRepository apprenantRepository;
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = reservationRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setApprenant(updated.getApprenant());
            existing.setFormation(updated.getFormation());
            return reservationRepository.save(existing);
        }
        return null;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }


    }



