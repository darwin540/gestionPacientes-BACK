package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.TipoTerapiaRequestDTO;
import com.gestionpacientes.dto.TipoTerapiaResponseDTO;
import com.gestionpacientes.dto.TipoTerapiaUpdateDTO;
import com.gestionpacientes.entity.TipoTerapia;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.mapper.TipoTerapiaMapper;
import com.gestionpacientes.repository.TipoTerapiaRepository;
import com.gestionpacientes.service.TipoTerapiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoTerapiaServiceImpl implements TipoTerapiaService {

    private final TipoTerapiaRepository tipoTerapiaRepository;
    private final TipoTerapiaMapper tipoTerapiaMapper;

    @Override
    public TipoTerapiaResponseDTO crearTipoTerapia(TipoTerapiaRequestDTO requestDTO) {
        // Validar que no exista un tipo de terapia con el mismo nombre
        if (tipoTerapiaRepository.existsByNombre(requestDTO.getNombre())) {
            throw new DuplicateResourceException(
                "Ya existe un tipo de terapia con el nombre: " + requestDTO.getNombre()
            );
        }

        TipoTerapia tipoTerapia = tipoTerapiaMapper.toEntity(requestDTO);
        TipoTerapia tipoTerapiaGuardado = tipoTerapiaRepository.save(tipoTerapia);
        return tipoTerapiaMapper.toResponseDTO(tipoTerapiaGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTerapiaResponseDTO obtenerTipoTerapiaPorId(Long id) {
        TipoTerapia tipoTerapia = tipoTerapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de terapia", id));
        return tipoTerapiaMapper.toResponseDTO(tipoTerapia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoTerapiaResponseDTO> obtenerTodosLosTiposTerapia() {
        return tipoTerapiaRepository.findAll()
                .stream()
                .map(tipoTerapiaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TipoTerapiaResponseDTO actualizarTipoTerapia(Long id, TipoTerapiaUpdateDTO updateDTO) {
        TipoTerapia tipoTerapia = tipoTerapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de terapia", id));

        // Validar que el nombre no esté duplicado (si se está actualizando)
        if (updateDTO.getNombre() != null && 
            !updateDTO.getNombre().equals(tipoTerapia.getNombre()) &&
            tipoTerapiaRepository.existsByNombre(updateDTO.getNombre())) {
            throw new DuplicateResourceException(
                "Ya existe otro tipo de terapia con el nombre: " + updateDTO.getNombre()
            );
        }

        tipoTerapiaMapper.updateEntityFromDTO(updateDTO, tipoTerapia);
        TipoTerapia tipoTerapiaActualizado = tipoTerapiaRepository.save(tipoTerapia);
        return tipoTerapiaMapper.toResponseDTO(tipoTerapiaActualizado);
    }

    @Override
    public void eliminarTipoTerapia(Long id) {
        if (!tipoTerapiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de terapia", id);
        }
        tipoTerapiaRepository.deleteById(id);
    }
}


