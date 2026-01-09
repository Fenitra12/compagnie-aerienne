package com.aerienne.gestion.model.reservations;

import java.time.LocalDateTime;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.model.prix.PrixVol;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Reservation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_passager", "id_prix_vol"}))
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservation;

    @ManyToOne
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @ManyToOne
    @JoinColumn(name = "id_prix_vol", nullable = false)
    private PrixVol prixVol;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    private LocalDateTime dateReservation;
    private String siege;
    private String statut;

    // getters & setters
}
