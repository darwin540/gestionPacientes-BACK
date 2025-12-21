package com.gestionpacientes.repository;

import com.gestionpacientes.constants.DatabaseQueries;
import com.gestionpacientes.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {
    
    @Query(DatabaseQueries.TERAPIA_FIND_BY_PACIENTE_ID)
    List<Terapia> findByPacienteId(@Param("pacienteId") Long pacienteId);
    
    @Query(DatabaseQueries.TERAPIA_FIND_BY_PROFESIONAL_ID)
    List<Terapia> findByProfesionalId(@Param("profesionalId") Long profesionalId);
    
    @Query(DatabaseQueries.TERAPIA_FIND_BY_PACIENTE_ID_AND_PROFESIONAL_ID)
    List<Terapia> findByPacienteIdAndProfesionalId(@Param("pacienteId") Long pacienteId, @Param("profesionalId") Long profesionalId);
    
    @Query(DatabaseQueries.TERAPIA_FIND_DISTINCT_PROFESIONAL_IDS_BY_PACIENTE_ID)
    List<Long> findDistinctProfesionalIdsByPacienteId(@Param("pacienteId") Long pacienteId);
    
}




