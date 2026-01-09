package com.aerienne.gestion.repository.reservations;

import org.springframework.stereotype.Repository;
import com.aerienne.gestion.model.reservations.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
}
