package com.gestionpacientes.repository;

import com.gestionpacientes.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    
    Optional<TipoDocumento> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    List<TipoDocumento> findByActivoTrue();
    
}



