package com.aerienne.gestion.model.vol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Vol_Place_Classe", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_vol", "classe"})
})
public class VolPlaceClasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVolPlace;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @Column(nullable = false)
    private String classe;

    @Column(nullable = false)
    private Integer seatsTotal;

    @Column(nullable = false)
    private Integer seatsAvailable;

    // Getters and Setters
    public Long getIdVolPlace() {
        return idVolPlace;
    }

    public void setIdVolPlace(Long idVolPlace) {
        this.idVolPlace = idVolPlace;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Integer getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(Integer seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}