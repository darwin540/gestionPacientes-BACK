package com.gestionpacientes.service;

import com.gestionpacientes.dto.TipoTerapiaRequestDTO;
import com.gestionpacientes.dto.TipoTerapiaResponseDTO;
import com.gestionpacientes.dto.TipoTerapiaUpdateDTO;

import java.util.List;

public interface TipoTerapiaService {

    TipoTerapiaResponseDTO crearTipoTerapia(TipoTerapiaRequestDTO requestDTO);

    TipoTerapiaResponseDTO obtenerTipoTerapiaPorId(Long id);

    List<TipoTerapiaResponseDTO> obtenerTodosLosTiposTerapia();

    TipoTerapiaResponseDTO actualizarTipoTerapia(Long id, TipoTerapiaUpdateDTO updateDTO);

    void eliminarTipoTerapia(Long id);
}

