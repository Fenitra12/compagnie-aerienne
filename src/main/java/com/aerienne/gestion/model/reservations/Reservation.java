package com.aerienne.gestion.model.reservations;

import java.time.LocalDateTime;

import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.model.prix.PrixVol;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @ManyToOne
    @JoinColumn(name = "id_prix_vol", nullable = false)
    private PrixVol prixVol;

    private LocalDateTime dateReservation;
    private String siege;
    private String statut;
    @jakarta.persistence.Column(name = "adult_count")
    private Integer adultCount;

    @jakarta.persistence.Column(name = "child_count")
    private Integer childCount;

    // getters & setters
    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public PrixVol getPrixVol() {
        return prixVol;
    }

    public void setPrixVol(PrixVol prixVol) {
        this.prixVol = prixVol;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

}
