package com.aerienne.gestion.model.vol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statutvol")
public class StatutVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut")
    private Long idStatut;

    @Column(name = "nom")
    private String nom;

    // Getters and Setters
    public Long getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(Long idStatut) {
        this.idStatut = idStatut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}