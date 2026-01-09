package com.aerienne.gestion.model.pilotes;

import com.aerienne.gestion.model.compagnies.Compagnie;
import jakarta.persistence.*;

@Entity
@Table(name = "Pilote")
public class Pilote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPilote;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private Compagnie compagnie;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String licenceNum;

    private int experience;

    // getters & setters
}
