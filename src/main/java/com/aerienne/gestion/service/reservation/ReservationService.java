package com.aerienne.gestion.service.reservation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.repository.reservation.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation) {
        if (reservation.getDateReservation() == null) {
            reservation.setDateReservation(LocalDateTime.now());
        }
        if (reservation.getStatut() == null || reservation.getStatut().isEmpty()) {
            reservation.setStatut("confirmée");
        }
        return reservationRepository.save(reservation);
    }
}
