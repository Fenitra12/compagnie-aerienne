package com.aerienne.gestion.service.avions;

import com.aerienne.gestion.model.avions.Avion;
import com.aerienne.gestion.repository.avions.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;

    public List<Avion> getAllAvions() {
        return avionRepository.findAll();
    }

    public Avion getAvionById(Long id) {
        Optional<Avion> avion = avionRepository.findById(id);
        return avion.orElse(null);
    }

    public void saveAvion(Avion avion) {
        avionRepository.save(avion);
    }

    public void deleteAvion(Long id) {
        avionRepository.deleteById(id);
    }
}
