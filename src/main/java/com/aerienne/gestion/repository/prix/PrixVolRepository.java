package com.aerienne.gestion.repository.prix;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.prix.PrixVol;

@Repository
public interface PrixVolRepository extends JpaRepository<PrixVol, Long> {
    List<PrixVol> findByVol_IdVol(Long idVol);

    void deleteByVol_IdVol(Long idVol);
}