package com.gestionpacientes.service;

import com.gestionpacientes.dto.ServicioRequestDTO;
import com.gestionpacientes.dto.ServicioResponseDTO;
import com.gestionpacientes.dto.ServicioUpdateDTO;

import java.util.List;

public interface ServicioService {

    ServicioResponseDTO crearServicio(ServicioRequestDTO requestDTO);

    ServicioResponseDTO obtenerServicioPorId(Long id);

    List<ServicioResponseDTO> obtenerTodosLosServicios();

    ServicioResponseDTO actualizarServicio(Long id, ServicioUpdateDTO updateDTO);

    void eliminarServicio(Long id);
}

