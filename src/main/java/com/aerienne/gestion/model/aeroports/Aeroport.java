package com.aerienne.gestion.model.aeroports;

import jakarta.persistence.*;

@Entity
@Table(name = "Aeroport")
public class Aeroport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAeroport;

    private String nom;
    private String ville;
    private String pays;

    @Column(length = 3, unique = true)
    private String codeIata;

    @Column(length = 4, unique = true)
    private String codeIcao;

    // Getters and Setters
    public Long getIdAeroport() {
        return idAeroport;
    }

    public void setIdAeroport(Long idAeroport) {
        this.idAeroport = idAeroport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCodeIata() {
        return codeIata;
    }

    public void setCodeIata(String codeIata) {
        this.codeIata = codeIata;
    }

    public String getCodeIcao() {
        return codeIcao;
    }

    public void setCodeIcao(String codeIcao) {
        this.codeIcao = codeIcao;
    }
}
