package com.aerienne.gestion.repository.vol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.vol.VolPlaceClasse;

@Repository
public interface VolPlaceClasseRepository extends JpaRepository<VolPlaceClasse, Long> {
    List<VolPlaceClasse> findByVol_IdVol(Long volId);
    VolPlaceClasse findByVol_IdVolAndClasse(Long volId, String classe);
    void deleteByVol_IdVol(Long volId);
}
