package com.aerienne.gestion.service.vol;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.vol.VolRepository;

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;

    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    public Vol saveVol(Vol vol) {
        if (vol.getIdVol() == null) {
            // Nouveau vol : initialiser les sièges
            vol.setSeatsTotal(vol.getAvion().getCapacite());
            vol.setSeatsAvailable(vol.getAvion().getCapacite());
        }
        return volRepository.save(vol);
    }

    public void deleteVol(Long id) {
        volRepository.deleteById(id);
    }

    public Vol getVolById(Long id) {
        return volRepository.findById(id).orElse(null);
    }

    public List<Object[]> findMaxRevenuParVol() {
        return volRepository.findMaxRevenuParVol();
    }

    public List<Map<String, Object>> findMaxRevenuParVolFiltered(Long departId, Long arriveeId) {
    return volRepository.findMaxRevenuParVolFiltered(departId, arriveeId);
}
}
