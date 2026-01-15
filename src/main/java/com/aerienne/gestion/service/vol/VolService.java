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

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private VolPlaceClasseRepository volPlaceClasseRepository;

    @Autowired
    private PrixVolRepository prixVolRepository;

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
    public void replaceClassesAndPrices(Vol vol, List<String> classes, List<Integer> seats, List<Double> prices) {
        if (vol == null || classes == null || seats == null || prices == null) {
            return;
        }

        // Nettoyer existant puis flusher pour éviter les conflits de contrainte uniques
        volPlaceClasseRepository.deleteByVol_IdVol(vol.getIdVol());
        volPlaceClasseRepository.flush();
        prixVolRepository.deleteAll(prixVolRepository.findByVol_IdVol(vol.getIdVol()));
        prixVolRepository.flush();

        var seen = new java.util.HashSet<String>();

        for (int i = 0; i < classes.size(); i++) {
            String cls = classes.get(i);
            if (cls == null || cls.isBlank()) {
                continue;
            }
            String clsKey = cls.trim();
            if (!seen.add(clsKey.toLowerCase())) {
                // Ignore doublons de classe pour éviter la contrainte UNIQUE (id_vol, classe)
                continue;
            }

            Integer seatTotal = seats.size() > i ? seats.get(i) : null;
            Double price = prices.size() > i ? prices.get(i) : null;
            if (seatTotal == null || seatTotal < 0 || price == null) {
                continue;
            }

            // Vol_Place_Classe
            VolPlaceClasse vpc = new VolPlaceClasse();
            vpc.setVol(vol);
            vpc.setClasse(clsKey);
            vpc.setSeatsTotal(seatTotal);
            vpc.setSeatsAvailable(seatTotal); // reset disponible = total saisi
            volPlaceClasseRepository.save(vpc);

            // Prix
            PrixVol pv = new PrixVol();
            pv.setVol(vol);
            pv.setClasse(clsKey);
            pv.setPrix(price);
            prixVolRepository.save(pv);
        }
    }

    public List<VolPlaceClasse> getClasses(Long volId) {
        return volPlaceClasseRepository.findByVol_IdVol(volId);
    }

    public List<PrixVol> getPrixByVol(Long volId) {
        return prixVolRepository.findByVol_IdVol(volId);
    }

    public void deleteVol(Long id) {
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