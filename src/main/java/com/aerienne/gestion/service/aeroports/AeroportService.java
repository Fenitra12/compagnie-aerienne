package com.aerienne.gestion.service.aeroports;

import com.aerienne.gestion.model.aeroports.Aeroport;
import com.aerienne.gestion.repository.aeroports.AeroportRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeroportService {

    @Autowired
    private AeroportRepository aeroportRepository;

    public List<Aeroport> getAllAeroports() {
        return aeroportRepository.findAll();
    }
}