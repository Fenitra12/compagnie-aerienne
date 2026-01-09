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

    // getters & setters
}
