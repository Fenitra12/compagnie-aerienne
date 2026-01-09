package com.aerienne.gestion.repository.avions;

import com.aerienne.gestion.model.avions.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Long> {
}
