package com.aerienne.gestion.service.passager;

import org.springframework.stereotype.Service;
import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.repository.passagers.PassagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class PassagerService {
    @Autowired
    private PassagerRepository passagerRepository;

    public List<Passager> getAllPassagers(){
        return passagerRepository.findAll();
    }

       public Passager getPassagerById(Long id) {
        Optional<Passager> passager = passagerRepository.findById(id);
        return passager.orElse(null);
    }

    public void savePassager(Passager passager) {
        passagerRepository.save(passager);
    }

    public void deletePassager(Long id) {
        passagerRepository.deleteById(id);
    }
}
