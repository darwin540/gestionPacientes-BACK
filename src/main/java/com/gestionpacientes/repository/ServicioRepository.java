package com.gestionpacientes.repository;

import com.gestionpacientes.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    
    Optional<Servicio> findByAbreviatura(String abreviatura);
}

