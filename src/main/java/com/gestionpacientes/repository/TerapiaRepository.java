package com.gestionpacientes.repository;

import com.gestionpacientes.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {
    
    List<Terapia> findByPacienteId(Long pacienteId);
    
    List<Terapia> findByProfesionalId(Long profesionalId);
    
    List<Terapia> findByPacienteIdAndProfesionalId(Long pacienteId, Long profesionalId);
    
}


