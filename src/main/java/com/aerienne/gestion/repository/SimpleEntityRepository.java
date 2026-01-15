package com.aerienne.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.SimpleEntity;

@Repository
public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, Long> {
}
