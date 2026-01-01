package com.gestionpacientes.repository;

import com.gestionpacientes.entity.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    
    Optional<Profesional> findByEmail(String email);
    
    Optional<Profesional> findByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
}

