package com.aerienne.gestion.model.vols;

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

    private String statut;

    // getters & setters
}
