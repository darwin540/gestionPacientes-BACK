package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.ProfesionalRequestDTO;
import com.gestionpacientes.dto.ProfesionalResponseDTO;
import com.gestionpacientes.dto.ProfesionalUpdateDTO;
import com.gestionpacientes.entity.Profesional;
import com.gestionpacientes.entity.TipoTerapia;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.mapper.ProfesionalMapper;
import com.gestionpacientes.repository.ProfesionalRepository;
import com.gestionpacientes.repository.TipoTerapiaRepository;
import com.gestionpacientes.service.ProfesionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfesionalServiceImpl implements ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final TipoTerapiaRepository tipoTerapiaRepository;
    private final ProfesionalMapper profesionalMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public ProfesionalResponseDTO crearProfesional(ProfesionalRequestDTO requestDTO) {
        // Validar que el email no exista
        if (profesionalRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException(
                "Ya existe un profesional con el email: " + requestDTO.getEmail()
            );
        }

        // Validar que el username no exista
        if (profesionalRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException(
                "Ya existe un profesional con el nombre de usuario: " + requestDTO.getUsername()
            );
        }

        Profesional profesional = profesionalMapper.toEntity(requestDTO);
        // Encriptar password
        profesional.setPassword(passwordEncoder.encode(profesional.getPassword()));
        
        // Asignar tipos de terapia si se proporcionan
        if (requestDTO.getTiposTerapiaIds() != null && !requestDTO.getTiposTerapiaIds().isEmpty()) {
            Set<TipoTerapia> tiposTerapia = obtenerTiposTerapiaPorIds(requestDTO.getTiposTerapiaIds());
            profesional.setTiposTerapia(tiposTerapia);
        }
        
        Profesional profesionalGuardado = profesionalRepository.save(profesional);
        return profesionalMapper.toResponseDTO(profesionalGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesionalResponseDTO obtenerProfesionalPorId(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));
        // Forzar carga de la colección lazy
        profesional.getTiposTerapia().size();
        return profesionalMapper.toResponseDTO(profesional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionalResponseDTO> obtenerTodosLosProfesionales() {
        return profesionalRepository.findAll()
                .stream()
                .map(profesional -> {
                    // Forzar carga de la colección lazy
                    profesional.getTiposTerapia().size();
                    return profesionalMapper.toResponseDTO(profesional);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProfesionalResponseDTO actualizarProfesional(Long id, ProfesionalUpdateDTO updateDTO) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", id));

        profesionalMapper.updateEntityFromDTO(updateDTO, profesional);
        
        // Actualizar tipos de terapia si se proporcionan
        if (updateDTO.getTiposTerapiaIds() != null) {
            Set<TipoTerapia> tiposTerapia = obtenerTiposTerapiaPorIds(updateDTO.getTiposTerapiaIds());
            profesional.setTiposTerapia(tiposTerapia);
        }
        
        Profesional profesionalActualizado = profesionalRepository.save(profesional);
        return profesionalMapper.toResponseDTO(profesionalActualizado);
    }

    @Override
    public void eliminarProfesional(Long id) {
        if (!profesionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesional", id);
        }
        profesionalRepository.deleteById(id);
    }

    @Override
    public ProfesionalResponseDTO asignarTiposTerapia(Long profesionalId, Set<Long> tiposTerapiaIds) {
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        Set<TipoTerapia> tiposTerapiaParaAsignar = obtenerTiposTerapiaPorIds(tiposTerapiaIds);
        profesional.getTiposTerapia().addAll(tiposTerapiaParaAsignar);

        Profesional profesionalActualizado = profesionalRepository.save(profesional);
        return profesionalMapper.toResponseDTO(profesionalActualizado);
    }

    @Override
    public ProfesionalResponseDTO quitarTiposTerapia(Long profesionalId, Set<Long> tiposTerapiaIds) {
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        Set<TipoTerapia> tiposTerapiaParaQuitar = obtenerTiposTerapiaPorIds(tiposTerapiaIds);
        profesional.getTiposTerapia().removeAll(tiposTerapiaParaQuitar);

        Profesional profesionalActualizado = profesionalRepository.save(profesional);
        return profesionalMapper.toResponseDTO(profesionalActualizado);
    }

    private Set<TipoTerapia> obtenerTiposTerapiaPorIds(Set<Long> tiposTerapiaIds) {
        Set<TipoTerapia> tiposTerapia = new HashSet<>();
        for (Long tipoTerapiaId : tiposTerapiaIds) {
            TipoTerapia tipoTerapia = tipoTerapiaRepository.findById(tipoTerapiaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de terapia", tipoTerapiaId));
            tiposTerapia.add(tipoTerapia);
        }
        return tiposTerapia;
    }
}

