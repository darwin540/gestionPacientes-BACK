package com.gestionpacientes.repository;

import com.gestionpacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio base para operaciones CRUD de pacientes.
 * Las consultas de búsqueda están en PacienteSearchRepository.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    /**
     * Busca un paciente por número de documento exacto.
     * Para búsquedas optimizadas con tipoDocumento, usar PacienteSearchRepository.
     */
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
    
    /**
     * Verifica si existe un paciente con el número de documento dado.
     */
    boolean existsByNumeroDocumento(String numeroDocumento);
}


