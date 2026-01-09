package com.aerienne.gestion.model.vol;

import com.aerienne.gestion.model.avions.Avion;
import com.aerienne.gestion.model.aeroports.Aeroport;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Vol")
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVol;

    @ManyToOne
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

    @ManyToOne
    @JoinColumn(name = "id_aeroport_depart", nullable = false)
    private Aeroport aeroportDepart;

    @ManyToOne
    @JoinColumn(name = "id_aeroport_arrivee", nullable = false)
    private Aeroport aeroportArrivee;

    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;

    @ManyToOne
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutVol statut;

    // Getters and Setters
    public Long getIdVol() {
        return idVol;
    }

    public void setIdVol(Long idVol) {
        this.idVol = idVol;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public StatutVol getStatut() {
        return statut;
    }

    public void setStatut(StatutVol statut) {
        this.statut = statut;
    }
}
