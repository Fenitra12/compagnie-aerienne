package com.aerienne.gestion.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerienne.gestion.model.pub.Publicite;

@Repository
public interface PubliciteRepository extends JpaRepository<Publicite, Long> {
}
