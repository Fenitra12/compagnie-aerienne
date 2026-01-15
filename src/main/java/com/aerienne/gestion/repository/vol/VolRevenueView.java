package com.aerienne.gestion.repository.vol;

import com.aerienne.gestion.model.vol.Vol;

public class VolRevenueView {

    private final Vol vol;
    private final Double revenue;
    private final Long reservationsCount;

    public VolRevenueView(Vol vol, Double revenue, Long reservationsCount) {
        this.vol = vol;
        this.revenue = revenue != null ? revenue : 0d;
        this.reservationsCount = reservationsCount != null ? reservationsCount : 0L;
    }

    public Vol getVol() {
        return vol;
    }

    public Double getRevenue() {
        return revenue;
    }

    public Long getReservationsCount() {
        return reservationsCount;
    }
}
