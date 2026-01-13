package com.aerienne.gestion.service.passagers;

import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.repository.passagers.PassagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassagerService {

    @Autowired
    private PassagerRepository passagerRepository;

    public List<Passager> getAllPassagers() {
        return passagerRepository.findAll();
    }

    public Passager savePassager(Passager passager) {
        return passagerRepository.save(passager);
    }

    public void deletePassager(Long id) {
        passagerRepository.deleteById(id);
    }

    public Passager getPassagerById(Long id) {
        return passagerRepository.findById(id).orElse(null);
    }
}