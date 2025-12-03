package com.gestionpacientes.service;

import com.gestionpacientes.dto.TipoDocumentoDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.TipoDocumento;
import com.gestionpacientes.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<TipoDocumentoDTO> findAll() {
        return tipoDocumentoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TipoDocumentoDTO> findAllActivos() {
        return tipoDocumentoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TipoDocumentoDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));
        return convertToDTO(tipoDocumento);
    }

    public TipoDocumentoDTO create(TipoDocumentoDTO tipoDocumentoDTO) {
        // Verificar si el nombre ya existe
        if (tipoDocumentoRepository.existsByNombre(tipoDocumentoDTO.getNombre())) {
            throw new DuplicateResourceException("El tipo de documento ya existe: " + tipoDocumentoDTO.getNombre());
        }

        TipoDocumento tipoDocumento = convertToEntity(tipoDocumentoDTO);
        TipoDocumento savedTipoDocumento = tipoDocumentoRepository.save(tipoDocumento);
        Objects.requireNonNull(savedTipoDocumento, "Error al guardar el tipo de documento");
        return convertToDTO(savedTipoDocumento);
    }

    public TipoDocumentoDTO update(Long id, TipoDocumentoDTO tipoDocumentoDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));

        // Verificar si el nombre ya existe en otro tipo de documento
        if (!tipoDocumento.getNombre().equals(tipoDocumentoDTO.getNombre())) {
            if (tipoDocumentoRepository.existsByNombre(tipoDocumentoDTO.getNombre())) {
                throw new DuplicateResourceException("El tipo de documento ya existe: " + tipoDocumentoDTO.getNombre());
            }
        }

        tipoDocumento.setNombre(tipoDocumentoDTO.getNombre());
        tipoDocumento.setDescripcion(tipoDocumentoDTO.getDescripcion());
        tipoDocumento.setActivo(tipoDocumentoDTO.getActivo());

        TipoDocumento updatedTipoDocumento = tipoDocumentoRepository.save(tipoDocumento);
        return convertToDTO(updatedTipoDocumento);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));
        
        // No eliminar físicamente si tiene pacientes asociados
        if (!tipoDocumento.getPacientes().isEmpty()) {
            // Desactivar en lugar de eliminar
            tipoDocumento.setActivo(false);
            tipoDocumentoRepository.save(tipoDocumento);
        } else {
            tipoDocumentoRepository.deleteById(id);
        }
    }

    // Métodos de conversión
    private TipoDocumentoDTO convertToDTO(TipoDocumento tipoDocumento) {
        Objects.requireNonNull(tipoDocumento, "El tipo de documento no puede ser nulo");
        return new TipoDocumentoDTO(
                tipoDocumento.getId(),
                tipoDocumento.getNombre(),
                tipoDocumento.getDescripcion(),
                tipoDocumento.getActivo()
        );
    }

    private TipoDocumento convertToEntity(TipoDocumentoDTO tipoDocumentoDTO) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNombre(tipoDocumentoDTO.getNombre());
        tipoDocumento.setDescripcion(tipoDocumentoDTO.getDescripcion());
        tipoDocumento.setActivo(tipoDocumentoDTO.getActivo() != null ? tipoDocumentoDTO.getActivo() : true);
        return tipoDocumento;
    }
}

