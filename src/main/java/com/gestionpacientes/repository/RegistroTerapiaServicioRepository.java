package com.gestionpacientes.repository;

import com.gestionpacientes.entity.RegistroTerapiaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroTerapiaServicioRepository extends JpaRepository<RegistroTerapiaServicio, Long> {

    List<RegistroTerapiaServicio> findByPacienteIdAndProfesionalId(Long pacienteId, Long profesionalId);

    List<RegistroTerapiaServicio> findByPacienteId(Long pacienteId);

    List<RegistroTerapiaServicio> findByProfesionalId(Long profesionalId);

    @Query("SELECT r FROM RegistroTerapiaServicio r WHERE r.paciente.id = :pacienteId AND r.profesional.id = :profesionalId AND r.tipoTerapia.id = :tipoTerapiaId AND r.servicio.id = :servicioId AND r.fecha = :fecha")
    Optional<RegistroTerapiaServicio> findByPacienteAndProfesionalAndTipoTerapiaAndServicioAndFecha(
            @Param("pacienteId") Long pacienteId,
            @Param("profesionalId") Long profesionalId,
            @Param("tipoTerapiaId") Long tipoTerapiaId,
            @Param("servicioId") Long servicioId,
            @Param("fecha") LocalDate fecha
    );
}

