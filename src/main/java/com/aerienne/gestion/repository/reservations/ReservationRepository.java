package com.aerienne.gestion.repository.reservations;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.repository.vol.VolRevenueView;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

		@Query("""
				SELECT new com.aerienne.gestion.repository.vol.VolRevenueView(v, SUM(p.prix), COUNT(r))
				FROM Reservation r
				JOIN r.prixVol p
				JOIN p.vol v
				JOIN v.avion a
				JOIN v.aeroportDepart ad
				JOIN v.aeroportArrivee aa
				WHERE (r.dateReservation IS NULL OR r.dateReservation BETWEEN :startDate AND :endDate)
					AND (:departId IS NULL OR ad.idAeroport = :departId)
					AND (:arriveeId IS NULL OR aa.idAeroport = :arriveeId)
					AND (:compagnieId IS NULL OR a.compagnie.idCompagnie = :compagnieId)
				GROUP BY v
				ORDER BY SUM(p.prix) DESC
				""")
		List<VolRevenueView> findRevenueByVol(@Param("startDate") LocalDateTime startDate,
																					@Param("endDate") LocalDateTime endDate,
																					@Param("departId") Long departId,
																					@Param("arriveeId") Long arriveeId,
																					@Param("compagnieId") Long compagnieId);
}