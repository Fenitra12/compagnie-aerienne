package com.aerienne.gestion.repository.pub;

import com.aerienne.gestion.model.pub.Societe;
import com.aerienne.gestion.model.vol.Vol;

public class VolSocieteRevenueView {
    private final Vol vol;
    private final Societe societe;
    private final double revenue;

    public VolSocieteRevenueView(Vol vol, Societe societe, Double revenue) {
        this.vol = vol;
        this.societe = societe;
        this.revenue = revenue != null ? revenue : 0d;
    }

    public Vol getVol() {
        return vol;
    }

    public Societe getSociete() {
        return societe;
    }

    public double getRevenue() {
        return revenue;
    }
}
