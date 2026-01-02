package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.ServicioRequestDTO;
import com.gestionpacientes.dto.ServicioResponseDTO;
import com.gestionpacientes.dto.ServicioUpdateDTO;
import com.gestionpacientes.entity.Servicio;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.mapper.ServicioMapper;
import com.gestionpacientes.repository.ServicioRepository;
import com.gestionpacientes.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    @Override
    public ServicioResponseDTO crearServicio(ServicioRequestDTO requestDTO) {
        Servicio servicio = servicioMapper.toEntity(requestDTO);
        Servicio servicioGuardado = servicioRepository.save(servicio);
        return servicioMapper.toResponseDTO(servicioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO obtenerServicioPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", id));
        return servicioMapper.toResponseDTO(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> obtenerTodosLosServicios() {
        return servicioRepository.findAll()
                .stream()
                .map(servicioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioResponseDTO actualizarServicio(Long id, ServicioUpdateDTO updateDTO) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", id));

        servicioMapper.updateEntityFromDTO(updateDTO, servicio);
        Servicio servicioActualizado = servicioRepository.save(servicio);
        return servicioMapper.toResponseDTO(servicioActualizado);
    }

    @Override
    public void eliminarServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio", id);
        }
        servicioRepository.deleteById(id);
    }
}


