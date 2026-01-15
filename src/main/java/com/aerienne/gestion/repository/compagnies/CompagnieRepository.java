package com.aerienne.gestion.repository.compagnies;

import com.aerienne.gestion.model.compagnies.Compagnie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompagnieRepository extends JpaRepository<Compagnie, Long> {
}
