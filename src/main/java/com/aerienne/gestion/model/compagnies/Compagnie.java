package com.aerienne.gestion.model.compagnies;

import jakarta.persistence.*;

@Entity
@Table(name = "Compagnie")
public class Compagnie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompagnie;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(length = 50)
    private String pays;

    @Column(length = 2, unique = true)
    private String codeIata;

    @Column(length = 3, unique = true)
    private String codeIcao;

    // Getters & Setters
    public Long getIdCompagnie() { return idCompagnie; }
    public void setIdCompagnie(Long idCompagnie) { this.idCompagnie = idCompagnie; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getCodeIata() { return codeIata; }
    public void setCodeIata(String codeIata) { this.codeIata = codeIata; }
    public String getCodeIcao() { return codeIcao; }
    public void setCodeIcao(String codeIcao) { this.codeIcao = codeIcao; }
}