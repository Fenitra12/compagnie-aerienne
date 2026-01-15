package com.aerienne.gestion.model.passagers;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Passager")
public class Passager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPassager;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    @Column(unique = true)
    private String email;

    // getters & setters
    public Long getIdPassager() {
        return idPassager;
    }

    public void setIdPassager(Long idPassager) {
        this.idPassager = idPassager;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
