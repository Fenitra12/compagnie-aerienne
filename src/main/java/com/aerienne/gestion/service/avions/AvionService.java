package com.aerienne.gestion.service.avions;

import com.aerienne.gestion.model.avions.Avion;
import com.aerienne.gestion.repository.avions.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;

    public List<Avion> getAllAvions() {
        return avionRepository.findAll();
    }
}
