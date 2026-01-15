package com.aerienne.gestion.repository.vol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.vol.StatutVol;

@Repository
public interface StatutVolRepository extends JpaRepository<StatutVol, Long> {
}