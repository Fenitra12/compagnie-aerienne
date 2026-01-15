package com.aerienne.gestion.service.vol;

import com.aerienne.gestion.model.vol.StatutVol;
import com.aerienne.gestion.repository.vol.StatutVolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatutVolService {

    @Autowired
    private StatutVolRepository statutVolRepository;

    public List<StatutVol> getAllStatuts() {
        return statutVolRepository.findAll();
    }
}