package com.gestionpacientes.service;

import com.gestionpacientes.dto.TipoTerapiaDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.TipoTerapia;
import com.gestionpacientes.repository.TipoTerapiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoTerapiaService {

    private final TipoTerapiaRepository tipoTerapiaRepository;

    @Autowired
    public TipoTerapiaService(TipoTerapiaRepository tipoTerapiaRepository) {
        this.tipoTerapiaRepository = tipoTerapiaRepository;
    }

    public List<TipoTerapiaDTO> findAll() {
        return tipoTerapiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TipoTerapiaDTO> findActivos() {
        return tipoTerapiaRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TipoTerapiaDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        TipoTerapia tipoTerapia = tipoTerapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTerapia", id));
        return convertToDTO(tipoTerapia);
    }

    public TipoTerapiaDTO create(TipoTerapiaDTO tipoTerapiaDTO) {
        if (tipoTerapiaRepository.existsByNombre(tipoTerapiaDTO.getNombre())) {
            throw new DuplicateResourceException("Ya existe un tipo de terapia con el nombre: " + tipoTerapiaDTO.getNombre());
        }
        TipoTerapia tipoTerapia = convertToEntity(tipoTerapiaDTO);
        @SuppressWarnings("null")
        TipoTerapia savedTipoTerapia = tipoTerapiaRepository.save(tipoTerapia);
        return convertToDTO(savedTipoTerapia);
    }

    public TipoTerapiaDTO update(Long id, TipoTerapiaDTO tipoTerapiaDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        TipoTerapia tipoTerapia = tipoTerapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTerapia", id));

        if (!tipoTerapia.getNombre().equalsIgnoreCase(tipoTerapiaDTO.getNombre()) && tipoTerapiaRepository.existsByNombre(tipoTerapiaDTO.getNombre())) {
            throw new DuplicateResourceException("Ya existe un tipo de terapia con el nombre: " + tipoTerapiaDTO.getNombre());
        }

        tipoTerapia.setNombre(tipoTerapiaDTO.getNombre());
        tipoTerapia.setValorUnitario(tipoTerapiaDTO.getValorUnitario());
        tipoTerapia.setActivo(tipoTerapiaDTO.getActivo());

        TipoTerapia updatedTipoTerapia = tipoTerapiaRepository.save(tipoTerapia);
        return convertToDTO(updatedTipoTerapia);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        if (!tipoTerapiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoTerapia", id);
        }
        // Aquí se podría añadir lógica para verificar si hay profesionales asociados antes de eliminar
        tipoTerapiaRepository.deleteById(id);
    }

    private TipoTerapiaDTO convertToDTO(TipoTerapia tipoTerapia) {
        Objects.requireNonNull(tipoTerapia, "El tipo de terapia no puede ser nulo");
        return new TipoTerapiaDTO(
                tipoTerapia.getId(),
                tipoTerapia.getNombre(),
                tipoTerapia.getValorUnitario(),
                tipoTerapia.getActivo()
        );
    }

    private TipoTerapia convertToEntity(TipoTerapiaDTO tipoTerapiaDTO) {
        TipoTerapia tipoTerapia = new TipoTerapia();
        tipoTerapia.setNombre(tipoTerapiaDTO.getNombre());
        tipoTerapia.setValorUnitario(tipoTerapiaDTO.getValorUnitario());
        tipoTerapia.setActivo(tipoTerapiaDTO.getActivo() != null ? tipoTerapiaDTO.getActivo() : true);
        return tipoTerapia;
    }
}



