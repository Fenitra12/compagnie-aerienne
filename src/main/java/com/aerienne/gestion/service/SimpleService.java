package com.aerienne.gestion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerienne.gestion.entity.SimpleEntity;
import com.aerienne.gestion.repository.SimpleEntityRepository;

@Service
public class SimpleService {
    @Autowired
    private SimpleEntityRepository simpleEntityRepository;

    // Exemple : calculer le pourcentage d'entités dont le nom commence par 'A'
    public double percentageNameStartsWithA() {
        List<SimpleEntity> all = simpleEntityRepository.findAll();
        if (all.isEmpty()) return 0.0;
        long count = all.stream().filter(e -> e.getName() != null && e.getName().startsWith("A")).count();
        return (count * 100.0) / all.size();
    }
}
