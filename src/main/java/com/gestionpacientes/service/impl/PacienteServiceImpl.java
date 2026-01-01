package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.PacienteRequestDTO;
import com.gestionpacientes.dto.PacienteResponseDTO;
import com.gestionpacientes.dto.PacienteUpdateDTO;
import com.gestionpacientes.entity.Paciente;
import com.gestionpacientes.entity.Profesional;
import com.gestionpacientes.entity.ProfesionalPaciente;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.mapper.PacienteMapper;
import com.gestionpacientes.repository.PacienteRepository;
import com.gestionpacientes.repository.ProfesionalPacienteRepository;
import com.gestionpacientes.repository.ProfesionalRepository;
import com.gestionpacientes.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final ProfesionalPacienteRepository profesionalPacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public PacienteResponseDTO crearPaciente(PacienteRequestDTO requestDTO, Long profesionalId) {
        // Validar que el profesional existe
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        // Validar que el paciente no exista ya para este profesional
        Paciente pacienteExistente = pacienteRepository
                .findByTipoDocumentoAndDocumento(requestDTO.getTipoDocumento(), requestDTO.getDocumento())
                .orElse(null);

        if (pacienteExistente != null && 
            profesionalPacienteRepository.existsByCreadoPorProfesionalIdAndPacienteId(
                    profesionalId, pacienteExistente.getId())) {
            throw new DuplicateResourceException(
                    "Ya existe un paciente con documento " + requestDTO.getTipoDocumento() + 
                    " " + requestDTO.getDocumento() + " creado por este profesional"
            );
        }

        // Buscar si el paciente ya existe en la base de datos
        Paciente paciente = pacienteRepository
                .findByTipoDocumentoAndDocumento(requestDTO.getTipoDocumento(), requestDTO.getDocumento())
                .orElse(null);

        // Si no existe, crearlo
        if (paciente == null) {
            paciente = pacienteMapper.toEntity(requestDTO);
            paciente = pacienteRepository.save(paciente);
        } else {
            // Si existe, validar que no esté ya asignado a este profesional
            if (profesionalPacienteRepository.existsByProfesionalIdAndPacienteId(profesionalId, paciente.getId())) {
                throw new DuplicateResourceException(
                        "Este paciente ya está asignado a este profesional"
                );
            }
        }

        // Crear la relación profesional-paciente
        ProfesionalPaciente profesionalPaciente = ProfesionalPaciente.builder()
                .profesional(profesional)
                .paciente(paciente)
                .creadoPorProfesionalId(profesionalId)
                .build();
        profesionalPacienteRepository.save(profesionalPaciente);

        return pacienteMapper.toResponseDTO(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDTO obtenerPacientePorId(Long id, Long profesionalId) {
        // Validar que el paciente pertenezca al profesional
        ProfesionalPaciente profesionalPaciente = profesionalPacienteRepository
                .findByProfesionalIdAndPacienteId(profesionalId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado o no tiene acceso"));

        return pacienteMapper.toResponseDTO(profesionalPaciente.getPaciente());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> obtenerTodosLosPacientes(Long profesionalId) {
        return profesionalPacienteRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(pp -> pacienteMapper.toResponseDTO(pp.getPaciente()))
                .collect(Collectors.toList());
    }

    @Override
    public PacienteResponseDTO actualizarPaciente(Long id, PacienteUpdateDTO updateDTO, Long profesionalId) {
        // Validar que el paciente pertenezca al profesional
        ProfesionalPaciente profesionalPaciente = profesionalPacienteRepository
                .findByProfesionalIdAndPacienteId(profesionalId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado o no tiene acceso"));

        // Solo el creador puede actualizar
        if (!profesionalPaciente.getCreadoPorProfesionalId().equals(profesionalId)) {
            throw new ResourceNotFoundException("No tiene permisos para actualizar este paciente");
        }

        Paciente paciente = profesionalPaciente.getPaciente();
        pacienteMapper.updateEntityFromDTO(updateDTO, paciente);
        Paciente pacienteActualizado = pacienteRepository.save(paciente);
        return pacienteMapper.toResponseDTO(pacienteActualizado);
    }

    @Override
    public void eliminarPaciente(Long id, Long profesionalId) {
        // Validar que el paciente pertenezca al profesional
        ProfesionalPaciente profesionalPaciente = profesionalPacienteRepository
                .findByProfesionalIdAndPacienteId(profesionalId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado o no tiene acceso"));

        // Solo el creador puede eliminar
        if (!profesionalPaciente.getCreadoPorProfesionalId().equals(profesionalId)) {
            throw new ResourceNotFoundException("No tiene permisos para eliminar este paciente");
        }

        // Eliminar la relación
        profesionalPacienteRepository.delete(profesionalPaciente);

        // Si no hay más relaciones con este paciente, eliminarlo de la base de datos
        if (!profesionalPacienteRepository.existsByPacienteId(profesionalPaciente.getPaciente().getId())) {
            pacienteRepository.delete(profesionalPaciente.getPaciente());
        }
    }
}

