package com.gestionpacientes.service;

import com.gestionpacientes.dto.ProfesionalRequestDTO;
import com.gestionpacientes.dto.ProfesionalResponseDTO;
import com.gestionpacientes.dto.ProfesionalUpdateDTO;

import java.util.List;
import java.util.Set;

public interface ProfesionalService {

    ProfesionalResponseDTO crearProfesional(ProfesionalRequestDTO requestDTO);

    ProfesionalResponseDTO obtenerProfesionalPorId(Long id);

    List<ProfesionalResponseDTO> obtenerTodosLosProfesionales();

    ProfesionalResponseDTO actualizarProfesional(Long id, ProfesionalUpdateDTO updateDTO);

    void eliminarProfesional(Long id);

    ProfesionalResponseDTO asignarTiposTerapia(Long profesionalId, Set<Long> tiposTerapiaIds);

    ProfesionalResponseDTO quitarTiposTerapia(Long profesionalId, Set<Long> tiposTerapiaIds);
}

