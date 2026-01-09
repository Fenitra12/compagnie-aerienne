package com.aerienne.gestion.service.compagnies;

import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.repository.compagnies.CompagnieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompagnieService {
    @Autowired
    private CompagnieRepository compagnieRepository;

    public List<Compagnie> getAllCompagnies() {
        return compagnieRepository.findAll();
    }
}
