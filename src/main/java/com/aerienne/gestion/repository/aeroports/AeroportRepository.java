package com.aerienne.gestion.repository.aeroports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.aeroports.Aeroport;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Long> {
}