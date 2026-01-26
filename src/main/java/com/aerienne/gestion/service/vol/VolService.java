package com.aerienne.gestion.service.vol;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.model.vol.VolPlaceClasse;
import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.repository.vol.VolRepository;
import com.aerienne.gestion.repository.vol.VolPlaceClasseRepository;
import com.aerienne.gestion.repository.prix.PrixVolRepository;
import com.aerienne.gestion.repository.vol.projection.VolClassRevenue;
import com.aerienne.gestion.repository.reservations.ReservationRepository;
import com.aerienne.gestion.repository.pub.DiffusionPubRepository;

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private VolPlaceClasseRepository volPlaceClasseRepository;

    @Autowired
    private PrixVolRepository prixVolRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DiffusionPubRepository diffusionPubRepository;

    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    public Vol saveVol(Vol vol) {
        // Laisser l'utilisateur saisir seatsTotal/seatsAvailable, mais borner seatsAvailable
        if (vol.getSeatsTotal() != null && vol.getSeatsAvailable() != null) {
            vol.setSeatsAvailable(Math.min(vol.getSeatsAvailable(), vol.getSeatsTotal()));
        }
        return volRepository.save(vol);
    }

    @Transactional
    public void replaceClassesAndPrices(Vol vol, List<String> classes, List<Integer> seats, List<Double> prices, List<Double> priceReductions, List<Double> babyPrices) {
        if (vol == null || classes == null || seats == null || prices == null) {
            return;
        }

        // Réinitialiser les places par classe (pas de FK sortante)
        volPlaceClasseRepository.deleteByVol_IdVol(vol.getIdVol());
        volPlaceClasseRepository.flush();

        var seen = new java.util.HashSet<String>();
        var existingPrix = prixVolRepository.findByVol_IdVol(vol.getIdVol())
                .stream().collect(java.util.stream.Collectors.toMap(PrixVol::getClasse, p -> p, (a, b) -> a, java.util.LinkedHashMap::new));
        var toKeep = new java.util.HashSet<String>();

        for (int i = 0; i < classes.size(); i++) {
            String cls = classes.get(i);
            if (cls == null || cls.isBlank()) {
                continue;
            }
            String clsKey = cls.trim();
            if (!seen.add(clsKey.toLowerCase())) {
                continue; // ignore doublons
            }

            Integer seatTotal = seats.size() > i ? seats.get(i) : null;
            Double price = prices.size() > i ? prices.get(i) : null;
            Double priceReduction = priceReductions != null && priceReductions.size() > i ? priceReductions.get(i) : null;
            Double priceBebe = babyPrices != null && babyPrices.size() > i ? babyPrices.get(i) : null;
            if (seatTotal == null || seatTotal < 0 || price == null) {
                continue;
            }

            // Vol_Place_Classe
            VolPlaceClasse vpc = new VolPlaceClasse();
            vpc.setVol(vol);
            vpc.setClasse(clsKey);
            vpc.setSeatsTotal(seatTotal);
            vpc.setSeatsAvailable(seatTotal);
            volPlaceClasseRepository.save(vpc);

            // Prix : update ou create
            PrixVol pv = existingPrix.getOrDefault(clsKey, new PrixVol());
            pv.setVol(vol);
            pv.setClasse(clsKey);
            pv.setPrix(price);
            pv.setPrixReduction(priceReduction != null && priceReduction > 0 ? priceReduction : price);
            double defaultBaby = price * 0.1;
            if (priceBebe != null && priceBebe > 0) {
                // Si la valeur est <= 100, on considère que c'est un pourcentage du prix adulte
                double babyValue = priceBebe <= 100 ? price * (priceBebe / 100d) : priceBebe;
                pv.setPrixBebe(babyValue);
            } else {
                pv.setPrixBebe(defaultBaby);
            }
            prixVolRepository.save(pv);
            toKeep.add(clsKey);
        }

        // Supprimer les prix orphelins uniquement si non référencés par des réservations
        for (var entry : existingPrix.entrySet()) {
            if (toKeep.contains(entry.getKey())) {
                continue;
            }
            PrixVol pv = entry.getValue();
            Long idPrix = pv.getIdPrix();
            if (idPrix != null) {
                long refs = reservationRepository.countByPrixVol_IdPrix(idPrix);
                if (refs == 0) {
                    prixVolRepository.delete(pv);
                }
            }
        }
        prixVolRepository.flush();
    }

    public List<VolPlaceClasse> getClasses(Long volId) {
        return volPlaceClasseRepository.findByVol_IdVol(volId);
    }

    public List<PrixVol> getPrixByVol(Long volId) {
        return prixVolRepository.findByVol_IdVol(volId);
    }

    @Transactional
    public void deleteVol(Long id) {
        long reservationCount = reservationRepository.countByPrixVol_Vol_IdVol(id);
        if (reservationCount > 0) {
            throw new IllegalStateException("Impossible de supprimer ce vol : des réservations existent.");
        }

        diffusionPubRepository.deleteByVol_IdVol(id);
        prixVolRepository.deleteByVol_IdVol(id);
        volPlaceClasseRepository.deleteByVol_IdVol(id);
        volRepository.deleteById(id);
    }

    public Vol getVolById(Long id) {
        return volRepository.findById(id).orElse(null);
    }

    public Double getMaxRevenue(Long volId) {
        return volRepository.calculateMaxRevenue(volId);
    }

    public List<VolClassRevenue> getClassRevenue(Long volId) {
        return volRepository.findClassRevenue(volId);
    }
}