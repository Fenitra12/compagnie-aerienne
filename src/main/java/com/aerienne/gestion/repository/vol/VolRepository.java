package com.aerienne.gestion.repository.vol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.vol.projection.VolClassRevenue;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {

	@Query(value = "SELECT COALESCE(SUM(vpc.seats_total * pv.prix), 0) AS revenue " +
				   "FROM Vol_Place_Classe vpc " +
				   "JOIN prix_vol pv ON pv.id_vol = vpc.id_vol AND pv.classe = vpc.classe " +
				   "WHERE vpc.id_vol = :volId", nativeQuery = true)
	Double calculateMaxRevenue(@Param("volId") Long volId);

	@Query(value = "SELECT vpc.classe AS classe, " +
				   "vpc.seats_total AS seatsTotal, " +
				   "pv.prix AS price, " +
				   "(vpc.seats_total * pv.prix) AS revenue " +
				   "FROM Vol_Place_Classe vpc " +
				   "JOIN prix_vol pv ON pv.id_vol = vpc.id_vol AND pv.classe = vpc.classe " +
				   "WHERE vpc.id_vol = :volId " +
				   "ORDER BY vpc.classe", nativeQuery = true)
	List<VolClassRevenue> findClassRevenue(@Param("volId") Long volId);
}