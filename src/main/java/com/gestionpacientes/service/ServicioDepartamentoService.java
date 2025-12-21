package com.gestionpacientes.service;

import com.gestionpacientes.dto.ServicioDepartamentoDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.ServicioDepartamento;
import com.gestionpacientes.repository.ServicioDepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioDepartamentoService {

    private final ServicioDepartamentoRepository servicioDepartamentoRepository;

    @Autowired
    public ServicioDepartamentoService(ServicioDepartamentoRepository servicioDepartamentoRepository) {
        this.servicioDepartamentoRepository = servicioDepartamentoRepository;
    }

    public List<ServicioDepartamentoDTO> findAll() {
        return servicioDepartamentoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServicioDepartamentoDTO> findAllActivos() {
        return servicioDepartamentoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ServicioDepartamentoDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", id));
        return convertToDTO(servicioDepartamento);
    }

    public ServicioDepartamentoDTO create(ServicioDepartamentoDTO servicioDepartamentoDTO) {
        // Verificar si la abreviación ya existe
        if (servicioDepartamentoRepository.existsByAbreviacion(servicioDepartamentoDTO.getAbreviacion())) {
            throw new DuplicateResourceException("La abreviación ya existe: " + servicioDepartamentoDTO.getAbreviacion());
        }

        // Verificar si el nombre ya existe
        if (servicioDepartamentoRepository.existsByNombre(servicioDepartamentoDTO.getNombre())) {
            throw new DuplicateResourceException("El nombre ya existe: " + servicioDepartamentoDTO.getNombre());
        }

        ServicioDepartamento servicioDepartamento = convertToEntity(servicioDepartamentoDTO);
        ServicioDepartamento savedServicioDepartamento = servicioDepartamentoRepository.save(servicioDepartamento);
        Objects.requireNonNull(savedServicioDepartamento, "Error al guardar el servicio/departamento");
        return convertToDTO(savedServicioDepartamento);
    }

    public ServicioDepartamentoDTO update(Long id, ServicioDepartamentoDTO servicioDepartamentoDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", id));

        // Verificar si la abreviación ya existe en otro servicio/departamento
        if (!servicioDepartamento.getAbreviacion().equals(servicioDepartamentoDTO.getAbreviacion())) {
            if (servicioDepartamentoRepository.existsByAbreviacion(servicioDepartamentoDTO.getAbreviacion())) {
                throw new DuplicateResourceException("La abreviación ya existe: " + servicioDepartamentoDTO.getAbreviacion());
            }
        }

        // Verificar si el nombre ya existe en otro servicio/departamento
        if (!servicioDepartamento.getNombre().equals(servicioDepartamentoDTO.getNombre())) {
            if (servicioDepartamentoRepository.existsByNombre(servicioDepartamentoDTO.getNombre())) {
                throw new DuplicateResourceException("El nombre ya existe: " + servicioDepartamentoDTO.getNombre());
            }
        }

        servicioDepartamento.setAbreviacion(servicioDepartamentoDTO.getAbreviacion());
        servicioDepartamento.setNombre(servicioDepartamentoDTO.getNombre());
        servicioDepartamento.setActivo(servicioDepartamentoDTO.getActivo());

        ServicioDepartamento updatedServicioDepartamento = servicioDepartamentoRepository.save(servicioDepartamento);
        return convertToDTO(updatedServicioDepartamento);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", id));
        
        // No eliminar físicamente si tiene terapias asociadas
        if (!servicioDepartamento.getTerapias().isEmpty()) {
            // Desactivar en lugar de eliminar
            servicioDepartamento.setActivo(false);
            servicioDepartamentoRepository.save(servicioDepartamento);
        } else {
            servicioDepartamentoRepository.deleteById(id);
        }
    }

    // Métodos de conversión
    private ServicioDepartamentoDTO convertToDTO(ServicioDepartamento servicioDepartamento) {
        Objects.requireNonNull(servicioDepartamento, "El servicio/departamento no puede ser nulo");
        return new ServicioDepartamentoDTO(
                servicioDepartamento.getId(),
                servicioDepartamento.getAbreviacion(),
                servicioDepartamento.getNombre(),
                servicioDepartamento.getActivo()
        );
    }

    private ServicioDepartamento convertToEntity(ServicioDepartamentoDTO servicioDepartamentoDTO) {
        ServicioDepartamento servicioDepartamento = new ServicioDepartamento();
        servicioDepartamento.setAbreviacion(servicioDepartamentoDTO.getAbreviacion());
        servicioDepartamento.setNombre(servicioDepartamentoDTO.getNombre());
        servicioDepartamento.setActivo(servicioDepartamentoDTO.getActivo() != null ? servicioDepartamentoDTO.getActivo() : true);
        return servicioDepartamento;
    }
}



