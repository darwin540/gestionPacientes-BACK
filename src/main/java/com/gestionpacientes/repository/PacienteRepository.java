package com.gestionpacientes.repository;

import com.gestionpacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Paciente> findByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);
    
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Paciente> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<Paciente> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);
    
}


