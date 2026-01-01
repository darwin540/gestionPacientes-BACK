package com.gestionpacientes.service;

import com.gestionpacientes.dto.PacienteRequestDTO;
import com.gestionpacientes.dto.PacienteResponseDTO;
import com.gestionpacientes.dto.PacienteUpdateDTO;

import java.util.List;

public interface PacienteService {

    PacienteResponseDTO crearPaciente(PacienteRequestDTO requestDTO, Long profesionalId);

    PacienteResponseDTO obtenerPacientePorId(Long id, Long profesionalId);

    List<PacienteResponseDTO> obtenerTodosLosPacientes(Long profesionalId);

    PacienteResponseDTO actualizarPaciente(Long id, PacienteUpdateDTO updateDTO, Long profesionalId);

    void eliminarPaciente(Long id, Long profesionalId);
}

