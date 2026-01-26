package com.aerienne.gestion.repository.pub;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.FacturePub;

@Repository
public interface FacturePubRepository extends JpaRepository<FacturePub, Long> {
    Optional<FacturePub> findBySociete_IdSociete(Long societeId);
}
