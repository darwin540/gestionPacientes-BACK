package com.gestionpacientes.repository;

import com.gestionpacientes.model.ServicioDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioDepartamentoRepository extends JpaRepository<ServicioDepartamento, Long> {
    
    Optional<ServicioDepartamento> findByAbreviacion(String abreviacion);
    
    Optional<ServicioDepartamento> findByNombre(String nombre);
    
    boolean existsByAbreviacion(String abreviacion);
    
    boolean existsByNombre(String nombre);
    
    List<ServicioDepartamento> findByActivoTrue();
    
}

