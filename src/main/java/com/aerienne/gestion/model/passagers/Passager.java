package com.aerienne.gestion.model.passagers;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    public Long getIdPassager() {
        return idPassager;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public String getEmail() {
        return email;
    }

}
