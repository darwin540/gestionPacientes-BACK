package com.gestionpacientes.repository;

import com.gestionpacientes.entity.Paciente;
import com.gestionpacientes.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);
}

