package com.aerienne.gestion.repository.pub;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.DiffusionPub;

@Repository
public interface DiffusionPubRepository extends JpaRepository<DiffusionPub, Long> {

    @Query("""
            SELECT new com.aerienne.gestion.repository.pub.PubRevenueView(
                v,
                SUM(d.nombreDiffusions * d.prixParDiffusion),
                SUM(d.nombreDiffusions)
            )
            FROM DiffusionPub d
            JOIN d.publicite p
            LEFT JOIN d.vol v
            LEFT JOIN v.avion a
            LEFT JOIN v.aeroportDepart ad
            LEFT JOIN v.aeroportArrivee aa
            WHERE (:requireVol = false OR v IS NOT NULL)
              AND (:departId IS NULL OR ad.idAeroport = :departId)
              AND (:arriveeId IS NULL OR aa.idAeroport = :arriveeId)
              AND (:compagnieId IS NULL OR a.compagnie.idCompagnie = :compagnieId)
              AND (:startYm IS NULL OR (d.annee * 100 + d.mois) >= :startYm)
              AND (:endYm IS NULL OR (d.annee * 100 + d.mois) <= :endYm)
            GROUP BY v
            ORDER BY SUM(d.nombreDiffusions * d.prixParDiffusion) DESC
            """)
    List<PubRevenueView> findRevenueByVol(@Param("startYm") Integer startYm,
                                          @Param("endYm") Integer endYm,
                                          @Param("departId") Long departId,
                                          @Param("arriveeId") Long arriveeId,
                                          @Param("compagnieId") Long compagnieId,
                                          @Param("requireVol") boolean requireVol);

    @Query("""
            SELECT new com.aerienne.gestion.repository.pub.VolSocieteRevenueView(
                v,
                p.societe,
                SUM(d.nombreDiffusions * d.prixParDiffusion)
            )
            FROM DiffusionPub d
            JOIN d.publicite p
            LEFT JOIN d.vol v
            LEFT JOIN v.avion a
            LEFT JOIN v.aeroportDepart ad
            LEFT JOIN v.aeroportArrivee aa
            WHERE (:requireVol = false OR v IS NOT NULL)
              AND (:departId IS NULL OR ad.idAeroport = :departId)
              AND (:arriveeId IS NULL OR aa.idAeroport = :arriveeId)
              AND (:compagnieId IS NULL OR a.compagnie.idCompagnie = :compagnieId)
              AND (:startYm IS NULL OR (d.annee * 100 + d.mois) >= :startYm)
              AND (:endYm IS NULL OR (d.annee * 100 + d.mois) <= :endYm)
            GROUP BY v, p.societe
            ORDER BY SUM(d.nombreDiffusions * d.prixParDiffusion) DESC
            """)
    List<VolSocieteRevenueView> findRevenueByVolAndSociete(@Param("startYm") Integer startYm,
                                                           @Param("endYm") Integer endYm,
                                                           @Param("departId") Long departId,
                                                           @Param("arriveeId") Long arriveeId,
                                                           @Param("compagnieId") Long compagnieId,
                                                           @Param("requireVol") boolean requireVol);

              @Query("""
                SELECT COALESCE(SUM(d.nombreDiffusions * d.prixParDiffusion), 0)
                FROM DiffusionPub d
                JOIN d.publicite p
                WHERE p.societe.idSociete = :societeId
                """)
              Double sumAmountBySociete(@Param("societeId") Long societeId);

    @Query("""
            SELECT d FROM DiffusionPub d
            WHERE d.publicite.idPublicite = :publiciteId
              AND ((:volId IS NULL AND d.vol IS NULL) OR (d.vol.idVol = :volId))
              AND d.annee = :annee
              AND d.mois = :mois
            """)
    java.util.Optional<DiffusionPub> findDuplicate(@Param("publiciteId") Long publiciteId,
                                                   @Param("volId") Long volId,
                                                   @Param("annee") Integer annee,
                                                   @Param("mois") Integer mois);

    void deleteByVol_IdVol(Long volId);
}
