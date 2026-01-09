package com.aerienne.gestion.service.utilisateurs;

import com.aerienne.gestion.model.utilisateurs.Utilisateur;
import com.aerienne.gestion.repository.utilisateurs.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getById(Integer id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Integer id) {
        utilisateurRepository.deleteById(id);
    }

    public Optional<Utilisateur> findByUsernameAndMotDePasse(String username, String motDePasse) {
        return utilisateurRepository.findByUsernameAndMotDePasse(username, motDePasse);
    }

    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }

    public Optional<Utilisateur> login(String username, String motDePasse) {
        return utilisateurRepository.findByUsernameAndMotDePasse(username, motDePasse);
    }
}


