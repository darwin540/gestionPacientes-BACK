package com.gestionpacientes.service;

import com.gestionpacientes.dto.ProfesionalDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.Profesional;
import com.gestionpacientes.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;

    @Autowired
    public ProfesionalService(ProfesionalRepository profesionalRepository) {
        this.profesionalRepository = profesionalRepository;
    }

    public List<ProfesionalDTO> findAll() {
        return profesionalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProfesionalDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));
        return convertToDTO(profesional);
    }

    public ProfesionalDTO findByNombreUsuario(String nombreUsuario) {
        Profesional profesional = profesionalRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional con nombre de usuario: " + nombreUsuario + " no encontrado"));
        return convertToDTO(profesional);
    }

    public ProfesionalDTO create(ProfesionalDTO profesionalDTO) {
        // Verificar si el nombre de usuario ya existe
        if (profesionalRepository.existsByNombreUsuario(profesionalDTO.getNombreUsuario())) {
            throw new DuplicateResourceException("El nombre de usuario ya existe: " + profesionalDTO.getNombreUsuario());
        }

        Profesional profesional = convertToEntity(profesionalDTO);
        @SuppressWarnings("null") // JPA save() siempre devuelve un objeto no nulo
        Profesional savedProfesional = profesionalRepository.save(profesional);
        return convertToDTO(savedProfesional);
    }

    public ProfesionalDTO update(Long id, ProfesionalDTO profesionalDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));

        // Verificar si el nombre de usuario ya existe en otro profesional
        if (!profesional.getNombreUsuario().equals(profesionalDTO.getNombreUsuario())) {
            if (profesionalRepository.existsByNombreUsuario(profesionalDTO.getNombreUsuario())) {
                throw new DuplicateResourceException("El nombre de usuario ya existe: " + profesionalDTO.getNombreUsuario());
            }
        }

        profesional.setNombre(profesionalDTO.getNombre());
        profesional.setNombreUsuario(profesionalDTO.getNombreUsuario());
        profesional.setProfesion(profesionalDTO.getProfesion());
        profesional.setTipoTerapia(profesionalDTO.getTipoTerapia());
        profesional.setValorPorTerapia(profesionalDTO.getValorPorTerapia());

        Profesional updatedProfesional = profesionalRepository.save(profesional);
        return convertToDTO(updatedProfesional);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        if (!profesionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional", id);
        }
        profesionalRepository.deleteById(id);
    }

    // Métodos de conversión
    private ProfesionalDTO convertToDTO(Profesional profesional) {
        Objects.requireNonNull(profesional, "El profesional no puede ser nulo");
        return new ProfesionalDTO(
                profesional.getId(),
                profesional.getNombre(),
                profesional.getNombreUsuario(),
                profesional.getProfesion(),
                profesional.getTipoTerapia(),
                profesional.getValorPorTerapia()
        );
    }

    private Profesional convertToEntity(ProfesionalDTO profesionalDTO) {
        Profesional profesional = new Profesional();
        profesional.setNombre(profesionalDTO.getNombre());
        profesional.setNombreUsuario(profesionalDTO.getNombreUsuario());
        profesional.setProfesion(profesionalDTO.getProfesion());
        profesional.setTipoTerapia(profesionalDTO.getTipoTerapia());
        profesional.setValorPorTerapia(profesionalDTO.getValorPorTerapia());
        return profesional;
    }
}

