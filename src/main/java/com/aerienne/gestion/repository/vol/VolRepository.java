package com.aerienne.gestion.repository.vol;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.vol.Vol;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {

    @Query("SELECT v.idVol as volId, "
            + "SUM(vpc.seatsTotal * pv.prix) as caMaximal "
            + "FROM Vol v "
            + "JOIN VolPlaceClasse vpc ON vpc.vol.idVol = v.idVol "
            + "JOIN PrixVol pv ON pv.vol.idVol = v.idVol AND pv.classe = vpc.classe "
            + "GROUP BY v.idVol "
            + "ORDER BY v.idVol")
    List<Object[]> findMaxRevenuParVol();

    @Query("SELECT new map(v.idVol as idVol, v.aeroportDepart.codeIata as depart, "
            + "v.aeroportArrivee.codeIata as arrivee, v.avion.modele as modele, "
            + "vpc.classe as classe, "
            + "vpc.seatsTotal as seatsTotal, "
            + "pv.prix as prix, "
            + "CAST(vpc.seatsTotal * pv.prix AS java.math.BigDecimal) as caClasse, "
            + "COALESCE(SUM(vpc.seatsTotal * pv.prix) OVER (PARTITION BY v.idVol), 0) as caMaximal) "
            + "FROM Vol v "
            + "LEFT JOIN VolPlaceClasse vpc ON vpc.vol.idVol = v.idVol "
            + "LEFT JOIN PrixVol pv ON pv.vol.idVol = v.idVol AND pv.classe = vpc.classe "
            + "WHERE (CAST(:depart AS Long) IS NULL OR v.aeroportDepart.idAeroport = :depart) "
            + "AND (CAST(:arrivee AS Long) IS NULL OR v.aeroportArrivee.idAeroport = :arrivee) "
            + "ORDER BY v.idVol, vpc.classe")
    List<Map<String, Object>> findMaxRevenuParVolFiltered(@Param("depart") Long depart, @Param("arrivee") Long arrivee);
}
