package com.aerienne.gestion.repository.passagers;

import org.springframework.stereotype.Repository;
import com.aerienne.gestion.model.passagers.Passager;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
    
}
