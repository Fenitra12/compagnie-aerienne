package com.aerienne.gestion.service.reservations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.reservations.ReservationRepository;
import com.aerienne.gestion.repository.vol.VolRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VolRepository volRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        // Vérifier la disponibilité des sièges
        Vol vol = reservation.getPrixVol().getVol();
        if (vol.getSeatsAvailable() <= 0) {
            throw new RuntimeException("Aucune place disponible pour ce vol.");
        }
        // Décrémenter les places disponibles
        vol.setSeatsAvailable(vol.getSeatsAvailable() - 1);
        volRepository.save(vol);
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            // Remettre la place disponible
            Vol vol = reservation.getPrixVol().getVol();
            vol.setSeatsAvailable(vol.getSeatsAvailable() + 1);
            volRepository.save(vol);
            reservationRepository.deleteById(id);
        }
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }
}