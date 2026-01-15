package com.aerienne.gestion.model.avions;

import com.aerienne.gestion.model.compagnies.Compagnie;
import jakarta.persistence.*;

@Entity
@Table(name = "Avion")
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvion;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private Compagnie compagnie;

    @Column(nullable = false, length = 50)
    private String modele;

    @Column(nullable = false)
    private int capacite;

    @Column(name = "numero_immatriculation", nullable = false, unique = true, length = 20)
    private String numeroImmatriculation;

    // Getters & Setters
    public Long getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getNumeroImmatriculation() {
        return numeroImmatriculation;
    }

    public void setNumeroImmatriculation(String numeroImmatriculation) {
        this.numeroImmatriculation = numeroImmatriculation;
    }
}
