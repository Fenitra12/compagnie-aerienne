package com.aerienne.gestion.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.Societe;

@Repository
public interface SocieteRepository extends JpaRepository<Societe, Long> {
}
