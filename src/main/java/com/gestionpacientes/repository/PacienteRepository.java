package com.gestionpacientes.repository;

import com.gestionpacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
    
}


