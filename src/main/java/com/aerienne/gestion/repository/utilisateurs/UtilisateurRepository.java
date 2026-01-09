package com.aerienne.gestion.repository.utilisateurs;

import com.aerienne.gestion.model.utilisateurs.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    
    Optional<Utilisateur> findByUsernameAndMotDePasse(String username, String motDePasse);
    
    Optional<Utilisateur> findByUsername(String username);
}


