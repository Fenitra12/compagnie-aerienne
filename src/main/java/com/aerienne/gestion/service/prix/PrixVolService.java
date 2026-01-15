package com.aerienne.gestion.service.prix;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.repository.prix.PrixVolRepository;

@Service
public class PrixVolService {

    @Autowired
    private PrixVolRepository prixVolRepository;

    public List<PrixVol> getAllPrixVols() {
        return prixVolRepository.findAll();
    }

    public PrixVol savePrixVol(PrixVol prixVol) {
        return prixVolRepository.save(prixVol);
    }

    public void deletePrixVol(Long id) {
        prixVolRepository.deleteById(id);
    }

    public PrixVol getPrixVolById(Long id) {
        return prixVolRepository.findById(id).orElse(null);
    }

    public List<PrixVol> findByVolId(Long volId) {
        return prixVolRepository.findByVol_IdVol(volId);
    }
}