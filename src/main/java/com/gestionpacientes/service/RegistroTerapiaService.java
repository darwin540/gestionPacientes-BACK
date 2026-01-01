package com.gestionpacientes.service;

import com.gestionpacientes.dto.RegistroTerapiaRequestDTO;
import com.gestionpacientes.dto.RegistroTerapiaResponseDTO;
import com.gestionpacientes.dto.RegistroTerapiaUpdateDTO;

import java.util.List;

public interface RegistroTerapiaService {

    List<RegistroTerapiaResponseDTO> crearRegistros(RegistroTerapiaRequestDTO requestDTO, Long profesionalId);

    RegistroTerapiaResponseDTO obtenerRegistroPorId(Long id, Long profesionalId);

    List<RegistroTerapiaResponseDTO> obtenerRegistrosPorPaciente(Long pacienteId, Long profesionalId);

    RegistroTerapiaResponseDTO actualizarRegistro(Long id, RegistroTerapiaUpdateDTO updateDTO, Long profesionalId);

    void eliminarRegistro(Long id, Long profesionalId);
}

