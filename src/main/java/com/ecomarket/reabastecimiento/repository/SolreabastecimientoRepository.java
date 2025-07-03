package com.ecomarket.reabastecimiento.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomarket.reabastecimiento.model.Solreabastecimiento;

@Repository
public interface SolreabastecimientoRepository extends JpaRepository<Solreabastecimiento, Long> {
    
}
