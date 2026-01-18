package com.aerienne.gestion.service.reservations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.model.vol.VolPlaceClasse;
import com.aerienne.gestion.repository.reservations.ReservationRepository;
import com.aerienne.gestion.repository.vol.VolPlaceClasseRepository;
import com.aerienne.gestion.repository.vol.VolRepository;
import com.aerienne.gestion.repository.vol.VolRevenueView;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private VolPlaceClasseRepository volPlaceClasseRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        int adults = reservation.getAdultCount() != null ? reservation.getAdultCount() : 0;
        int children = reservation.getChildCount() != null ? reservation.getChildCount() : 0;
        int babies = reservation.getBabyCount() != null ? reservation.getBabyCount() : 0;
        int totalPax = adults + children + babies;
        if (totalPax <= 0) {
            throw new RuntimeException("At least one passenger is required.");
        }

        // Vérifier la disponibilité des sièges
        Vol vol = reservation.getPrixVol().getVol();
        VolPlaceClasse placeClasse = volPlaceClasseRepository.findByVol_IdVolAndClasse(vol.getIdVol(), reservation.getPrixVol().getClasse());
        if (placeClasse == null) {
            throw new RuntimeException("Aucune configuration de places pour cette classe.");
        }
        if (placeClasse.getSeatsAvailable() < totalPax) {
            throw new RuntimeException("Pas assez de places disponibles dans cette classe.");
        }

        // Décrémenter les places disponibles par classe et globales
        placeClasse.setSeatsAvailable(placeClasse.getSeatsAvailable() - totalPax);
        volPlaceClasseRepository.save(placeClasse);

        vol.setSeatsAvailable(vol.getSeatsAvailable() - totalPax);
        volRepository.save(vol);
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            int adults = reservation.getAdultCount() != null ? reservation.getAdultCount() : 0;
            int children = reservation.getChildCount() != null ? reservation.getChildCount() : 0;
            int babies = reservation.getBabyCount() != null ? reservation.getBabyCount() : 0;
            int totalPax = adults + children + babies;
            // Remettre la place disponible
            Vol vol = reservation.getPrixVol().getVol();
            VolPlaceClasse placeClasse = volPlaceClasseRepository.findByVol_IdVolAndClasse(vol.getIdVol(), reservation.getPrixVol().getClasse());
            if (placeClasse != null) {
                placeClasse.setSeatsAvailable(placeClasse.getSeatsAvailable() + totalPax);
                volPlaceClasseRepository.save(placeClasse);
            }
            vol.setSeatsAvailable(vol.getSeatsAvailable() + totalPax);
            volRepository.save(vol);
            reservationRepository.deleteById(id);
        }
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

        public List<VolRevenueView> getVolRevenue(LocalDate startDate,
                                                  LocalDate endDate,
                                                  Long departId,
                                                  Long arriveeId,
                                                  Long compagnieId) {

            LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.of(1970, 1, 1, 0, 0);
            LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.of(3000, 1, 1, 0, 0);

            List<VolRevenueView> aggregated = reservationRepository.findRevenueByVol(start, end, departId, arriveeId, compagnieId);
        Map<Long, VolRevenueView> byVolId = aggregated.stream()
            .collect(Collectors.toMap(v -> v.getVol().getIdVol(), v -> v));

        Predicate<Vol> filter = vol -> (departId == null || Objects.equals(vol.getAeroportDepart().getIdAeroport(), departId))
            && (arriveeId == null || Objects.equals(vol.getAeroportArrivee().getIdAeroport(), arriveeId))
            && (compagnieId == null || Objects.equals(vol.getAvion().getCompagnie().getIdCompagnie(), compagnieId));

        volRepository.findAll().stream()
            .filter(filter)
            .filter(vol -> !byVolId.containsKey(vol.getIdVol()))
            .forEach(vol -> byVolId.put(vol.getIdVol(), new VolRevenueView(vol, 0d, 0L)));

        return byVolId.values().stream()
            .sorted((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()))
            .collect(Collectors.toList());
        }
}