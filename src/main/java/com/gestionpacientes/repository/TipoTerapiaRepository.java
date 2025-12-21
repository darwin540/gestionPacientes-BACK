package com.gestionpacientes.repository;

import com.gestionpacientes.model.TipoTerapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoTerapiaRepository extends JpaRepository<TipoTerapia, Long> {
    Optional<TipoTerapia> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<TipoTerapia> findByActivoTrue();
}



