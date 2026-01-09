package com.aerienne.gestion.service.vol;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.vol.VolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;

    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    public Vol saveVol(Vol vol) {
        return volRepository.save(vol);
    }

    public void deleteVol(Long id) {
        volRepository.deleteById(id);
    }

    public Vol getVolById(Long id) {
        return volRepository.findById(id).orElse(null);
    }
}