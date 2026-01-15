package com.aerienne.gestion.service.passagers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.repository.passagers.PassagerRepository;

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

    public Passager getByEmail(String email) {
        return passagerRepository.findByEmail(email);
    }
}