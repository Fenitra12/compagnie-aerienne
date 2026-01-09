package com.aerienne.gestion.model.prix;

import java.time.LocalDateTime;

import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.model.vol.Vol;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "prix_vol",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol", "id_compagnie", "classe"}))
public class PrixVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrix;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private Compagnie compagnie;

    private String classe;
    private Double prix;
    private LocalDateTime dateMaj;

    // getters & setters

    public Long getIdPrix() {
        return idPrix;
    }

    public void setIdPrix(Long idPrix) {
        this.idPrix = idPrix;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }
}
