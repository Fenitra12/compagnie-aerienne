package com.aerienne.gestion.service.compagnies;

import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.repository.compagnies.CompagnieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompagnieService {
    @Autowired
    private CompagnieRepository compagnieRepository;

    public List<Compagnie> getAllCompagnies() {
        return compagnieRepository.findAll();
    }

    public Compagnie getCompagnieById(Long id) {
        Optional<Compagnie> compagnie = compagnieRepository.findById(id);
        return compagnie.orElse(null);
    }

    public void saveCompagnie(Compagnie compagnie) {
        compagnieRepository.save(compagnie);
    }

    public void deleteCompagnie(Long id) {
        compagnieRepository.deleteById(id);
    }
}
