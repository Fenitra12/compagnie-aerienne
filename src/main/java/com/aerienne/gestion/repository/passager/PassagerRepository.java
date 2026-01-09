package com.aerienne.gestion.repository.passager;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.passagers.Passager;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
	Optional<Passager> findByEmail(String email);
}