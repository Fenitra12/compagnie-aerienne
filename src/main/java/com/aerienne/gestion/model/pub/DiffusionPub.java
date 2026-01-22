package com.aerienne.gestion.model.pub;

import com.aerienne.gestion.model.vol.Vol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "diffusion_pub")
public class DiffusionPub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDiffusion;

    @ManyToOne
    @JoinColumn(name = "id_publicite", nullable = false)
    private Publicite publicite;

    @ManyToOne
    @JoinColumn(name = "id_vol")
    private Vol vol;

    private Integer annee;
    private Integer mois;

    @Column(name = "nombre_diffusions")
    private Integer nombreDiffusions;

    @Column(name = "prix_par_diffusion")
    private Double prixParDiffusion;

    public Long getIdDiffusion() {
        return idDiffusion;
    }

    public void setIdDiffusion(Long idDiffusion) {
        this.idDiffusion = idDiffusion;
    }

    public Publicite getPublicite() {
        return publicite;
    }

    public void setPublicite(Publicite publicite) {
        this.publicite = publicite;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getNombreDiffusions() {
        return nombreDiffusions;
    }

    public void setNombreDiffusions(Integer nombreDiffusions) {
        this.nombreDiffusions = nombreDiffusions;
    }

    public Double getPrixParDiffusion() {
        return prixParDiffusion;
    }

    public void setPrixParDiffusion(Double prixParDiffusion) {
        this.prixParDiffusion = prixParDiffusion;
    }
}
