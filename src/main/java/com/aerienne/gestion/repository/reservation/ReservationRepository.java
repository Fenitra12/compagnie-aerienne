package com.aerienne.gestion.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.reservations.Reservation;

@Repository
@RepositoryRestResource(exported = false)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
}
