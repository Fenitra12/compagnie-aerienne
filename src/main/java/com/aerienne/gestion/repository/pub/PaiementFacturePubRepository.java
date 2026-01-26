package com.aerienne.gestion.repository.pub;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.PaiementFacturePub;

@Repository
public interface PaiementFacturePubRepository extends JpaRepository<PaiementFacturePub, Long> {
    List<PaiementFacturePub> findByFacture_IdFacture(Long factureId);
}
