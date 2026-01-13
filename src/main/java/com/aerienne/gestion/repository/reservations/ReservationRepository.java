package com.aerienne.gestion.repository.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.reservations.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}