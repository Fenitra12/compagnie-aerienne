package com.aerienne.gestion.repository.pub;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.PaiementPub;

@Repository
public interface PaiementPubRepository extends JpaRepository<PaiementPub, Long> {

    List<PaiementPub> findByDiffusionIdDiffusion(Long diffusionId);

    @Query("SELECT p.diffusion.idDiffusion, COALESCE(SUM(p.montant), 0) FROM PaiementPub p WHERE p.diffusion.idDiffusion IN :ids GROUP BY p.diffusion.idDiffusion")
    List<Object[]> sumByDiffusionIds(@Param("ids") List<Long> diffusionIds);
}
