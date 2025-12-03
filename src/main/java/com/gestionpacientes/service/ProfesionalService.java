package com.gestionpacientes.service;

import com.gestionpacientes.dto.ProfesionalDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.Profesional;
import com.gestionpacientes.repository.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfesionalService(ProfesionalRepository profesionalRepository, PasswordEncoder passwordEncoder) {
        this.profesionalRepository = profesionalRepository;
        this.passwordEncoder = passwordEncoder;
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
        // Normalizar nombre de usuario a mayúsculas y contraseña a minúsculas
        String nombreUsuarioNormalizado = profesionalDTO.getNombreUsuario().toUpperCase().trim();
        String passwordNormalizado = profesionalDTO.getPassword().toLowerCase().trim();
        
        // Verificar si el nombre de usuario ya existe (sin importar mayúsculas/minúsculas)
        if (profesionalRepository.existsByNombreUsuario(nombreUsuarioNormalizado)) {
            throw new DuplicateResourceException("El nombre de usuario ya existe: " + nombreUsuarioNormalizado);
        }

        profesionalDTO.setNombreUsuario(nombreUsuarioNormalizado);
        profesionalDTO.setPassword(passwordEncoder.encode(passwordNormalizado)); // Encriptar contraseña
        
        Profesional profesional = convertToEntity(profesionalDTO);
        @SuppressWarnings("null") // JPA save() siempre devuelve un objeto no nulo
        Profesional savedProfesional = profesionalRepository.save(profesional);
        return convertToDTO(savedProfesional);
    }

    public ProfesionalDTO update(Long id, ProfesionalDTO profesionalDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));

        // Normalizar nombre de usuario a mayúsculas
        String nombreUsuarioNormalizado = profesionalDTO.getNombreUsuario().toUpperCase().trim();
        String passwordNormalizado = profesionalDTO.getPassword() != null && !profesionalDTO.getPassword().isEmpty() 
            ? profesionalDTO.getPassword().toLowerCase().trim() 
            : profesional.getPassword();

        // Verificar si el nombre de usuario ya existe en otro profesional
        if (!profesional.getNombreUsuario().equalsIgnoreCase(nombreUsuarioNormalizado)) {
            if (profesionalRepository.existsByNombreUsuario(nombreUsuarioNormalizado)) {
                throw new DuplicateResourceException("El nombre de usuario ya existe: " + nombreUsuarioNormalizado);
            }
        }

        profesional.setNombre(profesionalDTO.getNombre());
        profesional.setApellido(profesionalDTO.getApellido());
        profesional.setNombreUsuario(nombreUsuarioNormalizado);
        if (profesionalDTO.getPassword() != null && !profesionalDTO.getPassword().isEmpty()) {
            profesional.setPassword(passwordEncoder.encode(passwordNormalizado)); // Encriptar contraseña
        }
        profesional.setProfesion(profesionalDTO.getProfesion());
        profesional.setTipoTerapia(profesionalDTO.getTipoTerapia());
        profesional.setActivo(profesionalDTO.getActivo() != null ? profesionalDTO.getActivo() : profesional.getActivo());

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

    public List<ProfesionalDTO> findAllActivos() {
        return profesionalRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updatePassword(Long id, String newPassword) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Objects.requireNonNull(newPassword, "La contraseña no puede ser nula");
        
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));
        
        // Normalizar contraseña a minúsculas y encriptar
        String passwordNormalizado = newPassword.toLowerCase().trim();
        profesional.setPassword(passwordEncoder.encode(passwordNormalizado));
        
        profesionalRepository.save(profesional);
    }

    // Métodos de conversión
    private ProfesionalDTO convertToDTO(Profesional profesional) {
        Objects.requireNonNull(profesional, "El profesional no puede ser nulo");
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getId());
        dto.setNombre(profesional.getNombre());
        dto.setApellido(profesional.getApellido());
        dto.setNombreUsuario(profesional.getNombreUsuario());
        // No establecer password por seguridad
        dto.setProfesion(profesional.getProfesion());
        dto.setTipoTerapia(profesional.getTipoTerapia());
        dto.setActivo(profesional.getActivo());
        return dto;
    }

    private Profesional convertToEntity(ProfesionalDTO profesionalDTO) {
        Profesional profesional = new Profesional();
        profesional.setNombre(profesionalDTO.getNombre());
        profesional.setApellido(profesionalDTO.getApellido());
        profesional.setNombreUsuario(profesionalDTO.getNombreUsuario());
        profesional.setPassword(profesionalDTO.getPassword());
        profesional.setProfesion(profesionalDTO.getProfesion());
        profesional.setTipoTerapia(profesionalDTO.getTipoTerapia());
        profesional.setActivo(profesionalDTO.getActivo() != null ? profesionalDTO.getActivo() : true);
        return profesional;
    }
}

