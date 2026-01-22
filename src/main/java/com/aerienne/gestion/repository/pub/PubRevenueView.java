package com.aerienne.gestion.repository.pub;

import com.aerienne.gestion.model.vol.Vol;

public class PubRevenueView {

    private final Vol vol;
    private final Double revenue;
    private final Long diffusionsCount;

    public PubRevenueView(Vol vol, Double revenue, Long diffusionsCount) {
        this.vol = vol;
        this.revenue = revenue != null ? revenue : 0d;
        this.diffusionsCount = diffusionsCount != null ? diffusionsCount : 0L;
    }

    public Vol getVol() {
        return vol;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Long getDiffusionsCount() {
        return diffusionsCount;
    }
}
