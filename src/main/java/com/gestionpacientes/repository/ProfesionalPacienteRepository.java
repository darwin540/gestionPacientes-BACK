package com.gestionpacientes.repository;

import com.gestionpacientes.entity.ProfesionalPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesionalPacienteRepository extends JpaRepository<ProfesionalPaciente, Long> {

    List<ProfesionalPaciente> findByProfesionalId(Long profesionalId);

    List<ProfesionalPaciente> findByCreadoPorProfesionalId(Long creadoPorProfesionalId);

    Optional<ProfesionalPaciente> findByProfesionalIdAndPacienteId(Long profesionalId, Long pacienteId);

    boolean existsByProfesionalIdAndPacienteId(Long profesionalId, Long pacienteId);

    boolean existsByCreadoPorProfesionalIdAndPacienteId(Long creadoPorProfesionalId, Long pacienteId);

    boolean existsByPacienteId(Long pacienteId);

    @Query("SELECT pp FROM ProfesionalPaciente pp WHERE pp.creadoPorProfesionalId = :profesionalId AND pp.paciente.tipoDocumento = :tipoDocumento AND pp.paciente.documento = :documento")
    Optional<ProfesionalPaciente> findByCreadorAndPacienteDocumento(
            @Param("profesionalId") Long profesionalId,
            @Param("tipoDocumento") String tipoDocumento,
            @Param("documento") String documento
    );
}

